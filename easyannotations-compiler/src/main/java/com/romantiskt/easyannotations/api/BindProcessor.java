package com.romantiskt.easyannotations.api;

import com.google.auto.service.AutoService;
import com.romantiskt.easyannotations.annotations.Bind;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;


/**
 * Created by romantiskt on 2018/8/15.
 */
@AutoService(Processor.class)
public class BindProcessor extends AbstractProcessor {

    public static final String BINDING = "_Binding";
    private Map<String, BindObject> map = new HashMap<>();
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Bind.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 注解的处理逻辑
     *
     * @return 如果返回true 不要求后续Processor处理它们，反之，则继续执行处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Bind.class);
        if (elements.isEmpty()) {
            return false;
        }
        for (Element element : elements) {
            //获取注解对象所在类的类名
            String  enclosingName = element.getEnclosingElement().toString();
            if (map.get(enclosingName) == null) {
                map.put(enclosingName, new BindObject(element));
            } else {
                map.get(enclosingName).addField(element);
            }
        }
        for (Map.Entry<String, BindObject> entry : map.entrySet()) {
            BindObject object = entry.getValue();
            createFile(object.getPackageName(), object.getClassType());
        }
        return false;
    }


    /**
     * 打印日志
     *
     * @param level 日志级别
     * @param msg   日志内容
     */
    private void log(Diagnostic.Kind level, String msg) {
        messager.printMessage(level, msg);
    }

    /**
     * 打印日志
     *
     * @param msg 日志内容
     */
    private void log(String msg) {
        log(Diagnostic.Kind.NOTE, msg);
    }


    /**
     * 生成类文件
     */
    private void createFile(String pName, TypeSpec typeSpec) {
        log("typeSpec: " + typeSpec.name);
        JavaFile javaFile = JavaFile.builder(pName, typeSpec)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            System.out.print("IOException");
            e.printStackTrace();
        }
    }
}
