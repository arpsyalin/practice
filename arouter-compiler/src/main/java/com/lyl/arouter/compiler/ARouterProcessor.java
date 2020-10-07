package com.lyl.arouter.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

import com.lyl.arouter.annotations.IInterceptor;
import com.lyl.arouter.annotations.IsActivity;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

@AutoService(Processor.class)
public class ARouterProcessor extends BaseProcessor {
    @Override
    protected Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(IsActivity.class);
        return annotations;
    }


    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        Set<? extends Element> clazzElement = env.getElementsAnnotatedWith(IsActivity.class);
        System.out.println("ARouterProcessor:" + env.processingOver() + ";size:" + clazzElement);
        if (clazzElement != null && clazzElement.size() > 0) {
            buildFile(clazzElement);
        }
        return false;
    }

    private void buildFile(Set<? extends Element> clazzElement) {
        try {
            String activityClassNameStr = "ARouterUtil_" + System.currentTimeMillis();
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activityClassNameStr).addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
            System.out.println(activityClassNameStr + clazzElement.size());
            MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder();
            constructorMethodBuilder.addModifiers(Modifier.PUBLIC);
            for (Element object : clazzElement) {
                String key = object.getAnnotation(IsActivity.class).value();
                String packageName = mElementUtils.getPackageOf(object).getQualifiedName().toString();
                String className = object.getSimpleName().toString();
                System.out.println(key);
                System.out.println(className);
                System.out.println(packageName);
                //object.getAnnotation(IsActivity.class).interceptor().getCanonicalName();不能直接这么获取会报错
                String interceptorName = getClassNameByAnnotation(object, IsActivity.class, "interceptor");
                System.out.println(interceptorName);
                constructorMethodBuilder.addStatement("ARouter.getInstance().addRouterData($S,$L.$L.class)", key, packageName, className);
                if (interceptorName != null && interceptorName.length() > 0 && !interceptorName.equals("com.lyl.arouter.annotations.DefaultInterceptor")) {
                    constructorMethodBuilder.addStatement("ARouter.getInstance().addInterceptor($S,$L.class)", key, interceptorName);
                }
            }
            classBuilder.addMethod(constructorMethodBuilder.build());
            JavaFile javaFile = JavaFile.builder("com.lyl.arouter", classBuilder.build()).build();
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected String getClassNameByAnnotation(Element object, Class<? extends Annotation> clazz, String valueName) {
        try {
            AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(object, clazz).get();
            Set<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> entrySet = annotationMirror.getElementValues().entrySet();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> value : entrySet) {
                if (value.getKey().getSimpleName().contentEquals(valueName)) {
                    DeclaredType declaredType = (DeclaredType) value.getValue().getValue();
                    TypeElement typeElement = (TypeElement) declaredType.asElement();
                    return typeElement.getQualifiedName().toString();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
