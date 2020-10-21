package com.lyl.arouter.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

import com.lyl.arouter.annotations.IsActivity;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
        if (clazzElement != null && clazzElement.size() > 0) {
            //构建ARouter文件
            buildARouterUtilFile(clazzElement);
            buildARouterConstantFile(clazzElement);
        }
        return false;
    }

    //构建常量文件
    private void buildARouterConstantFile(Set<? extends Element> clazzElement) {
        File savePath = new File("../arouter-constant/src/main/java/com/lyl/arouter/constant/ARouterConstant.java");
        if (savePath.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(savePath);
                FileChannel inputStreamFileChannel = fileInputStream.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 100);
                inputStreamFileChannel.read(buffer);
                buffer.flip();
                String data = decode(buffer);
                System.out.println("inputStreamFileChannel:" + data);
                String[] oldFiled = data.split("\n");
                List<String> newFiled = new ArrayList();

                for (Element object : clazzElement) {
                    String key = object.getAnnotation(IsActivity.class).value();
                    String className = object.getSimpleName().toString();
                    String thisFiled = "    public final static String " + className.toUpperCase() + "=\"" + key + "\";";
                    newFiled.add(thisFiled);
                    System.out.println("thisFiled:" + thisFiled);

                }
                for (String filed : oldFiled) {
                    if (filed.startsWith("    public final static String") && !newFiled.contains(filed)) {
                        newFiled.add(filed);
                    }
                }

                String[] mergeFiled = new String[newFiled.size()];
                for (int i = 0; i < newFiled.size(); i++) {
                    mergeFiled[i] = newFiled.get(i) + "\n";
                    System.out.println("mergeFiled:" + mergeFiled[i]);
                }
                savePath.delete();
                buildARouterConstantFile(savePath, mergeFiled);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            int len = clazzElement.size();
            String[] buffer = new String[len];
            int i = 0;
            for (Element object : clazzElement) {
                String key = object.getAnnotation(IsActivity.class).value();
                String className = object.getSimpleName().toString();
                buffer[i] = "    public final static String " + className.toUpperCase() + "=\"" + key + "\";\n";
                System.out.println("buildARouterConstantFile:" + buffer[i]);
                i++;
            }
            buildARouterConstantFile(savePath, buffer);
        }
    }

    //构建常量文件
    private void buildARouterConstantFile(File savePath, String... fileds) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(savePath);
            FileChannel channel = fileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 100);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("package com.lyl.arouter.constant;\n");
            stringBuffer.append("\n");
            stringBuffer.append("public class ARouterConstant\n");
            stringBuffer.append("{\n");
            for (String filed : fileds) {
                stringBuffer.append(filed);
            }
            stringBuffer.append("\n}");
            System.out.println(stringBuffer);
            buffer.put(stringBuffer.toString().getBytes());
            buffer.flip();
            //将内容写到通道中
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //将String转ByteBuffer
    private ByteBuffer convertStringToByte(String content) throws UnsupportedEncodingException {
        return ByteBuffer.wrap(content.getBytes("utf-8"));
    }

    //将ByteBuffer转String
    public String decode(ByteBuffer bb) {
        Charset charset = Charset.forName("utf-8");
        return charset.decode(bb).toString();
    }

    //构建ARouterUtil
    private void buildARouterUtilFile(Set<? extends Element> clazzElement) {
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

    //通过Annotation类获取类名
    private String getClassNameByAnnotation(Element object, Class<? extends Annotation> clazz, String valueName) {
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
