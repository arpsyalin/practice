package com.lyl.butterknife.compiler;

import com.google.auto.service.AutoService;
import com.lyl.butterknife.annotations.BindView;
import com.lyl.butterknife.annotations.OnClick;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

@AutoService(Processor.class)
public final class ButterKnifeProcessor extends BaseProcessor {
    @Override
    protected Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class);
        annotations.add(OnClick.class);
        return annotations;
    }

    public BuildObject getBuildObject(Class<? extends Annotation> clazz, Element element) {
        return BuildFactory.getInstance().getBuildObject(clazz, element);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
//        System.out.println("ButterKnifeProcessor:" + env.processingOver());
//        System.out.println("ButterKnifeProcessor:" + env.processingOver() + ";RoundEnvironment:size:" + env);
        Map<Element, List<BuildObject>> elementListMap = findBuildObject(env);
        buildFile(elementListMap);
        return false;
    }

    private void buildFile(Map<Element, List<BuildObject>> elementListMap) {
        for (Map.Entry<Element, List<BuildObject>> entry : elementListMap.entrySet()) {
            Element enclosingElement = entry.getKey();
            List<BuildObject> viewBindElements = entry.getValue();
            String activityClassNameStr = enclosingElement.getSimpleName().toString();
            ClassName activityClassName = ClassName.bestGuess(activityClassNameStr);
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activityClassNameStr + "$ViewBinding")
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
            buildElementConstructorMethod(activityClassName, classBuilder, viewBindElements);

            try {
                String packageName = mElementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
                JavaFile.builder(packageName, classBuilder.build())
                        .build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }


    private void buildElementConstructorMethod(ClassName activityClassName, TypeSpec.Builder classBuilder, List<BuildObject> buildObject) {
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(activityClassName, "target", Modifier.FINAL);
        for (BuildObject object : buildObject) {
            for (Statement statement : object.getStatement()) {
                constructorMethodBuilder.addStatement(statement.getFormat(), statement.getArgs());
            }
        }
        classBuilder.addMethod(constructorMethodBuilder.build());
    }


    /**
     * 查找建造对象
     * 从RoundEnvironment查找所有的相应的Annotation的注解对象
     * 以类文件Element为Key将添加进Map
     *
     * @param env
     * @return
     */
    public Map<Element, List<BuildObject>> findBuildObject(RoundEnvironment env) {
        Map<Element, List<BuildObject>> elementListMap = new LinkedHashMap<>();
        for (Class<? extends Annotation> clazz : getSupportedAnnotations()) {
            for (Element element : env.getElementsAnnotatedWith(clazz)) {
                try {
                    Element enclosingElement = element.getEnclosingElement();
                    List<BuildObject> elements = elementListMap.get(enclosingElement);
                    BuildObject buildObject = getBuildObject(clazz, element);
                    if (buildObject != null) {
                        if (elements == null) {
                            elements = new ArrayList<>();
                        }
                        elements.add(buildObject);
                        elementListMap.put(enclosingElement, elements);
                    }
                } catch (Exception e) {
                }
            }
        }
        return elementListMap;
    }
}
