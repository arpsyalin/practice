package com.lyl.butterknife.compiler;

import com.google.auto.service.AutoService;
import com.lyl.butterknife.annotations.BindView;
import com.lyl.butterknife.annotations.OnClick;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

@AutoService(Processor.class)
public final class ButterKnifeProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        Map<Element, List<BuildObject>> elementListMap = findBuildObject(env);
        buildFile(elementListMap);
        return true;
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
            // 生成类，看下效果
            try {
                String packageName = mElementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
                JavaFile.builder(packageName, classBuilder.build())
                        .addFileComment("自动生成")
                        .build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    private void buildElementConstructorMethod(ClassName activityClassName, TypeSpec.Builder classBuilder, List<BuildObject> buildObject) {
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder()
                .addParameter(activityClassName, "target",Modifier.FINAL) ;
        for (BuildObject object : buildObject) {
            for (Statement statement : object.getStatement()) {
                constructorMethodBuilder.addStatement(statement.format, statement.args);
            }
        }
        classBuilder.addMethod(constructorMethodBuilder.build());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class);
        annotations.add(OnClick.class);
        return annotations;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    /**
     * 查找建造对象
     * 从RoundEnvironment查找所有的相应的Annotation的注解对象
     * 以类文件Element为Key将添加进Map
     *
     * @param env
     * @return
     */
    private Map<Element, List<BuildObject>> findBuildObject(RoundEnvironment env) {
        Map<Element, List<BuildObject>> elementListMap = new LinkedHashMap<>();
        for (Class<? extends Annotation> clazz : getSupportedAnnotations()) {
            for (Element element : env.getElementsAnnotatedWith(clazz)) {
                try {
                    Element enclosingElement = element.getEnclosingElement();
                    List<BuildObject> elements = elementListMap.get(enclosingElement);
                    BuildObject buildObject = BuildFactory.getInstance().getBuildObject(clazz, element);
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
