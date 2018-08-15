package com.romantiskt.easyannotations.api;

import com.romantiskt.easyannotations.annotations.Bind;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
/**
 * Created by romantiskt on 2018/8/15.
 */
public class BindObject {

    private static final String TARGET = "target";
    private static final String VIEW = "view";

    private TypeSpec.Builder builder;
    private MethodSpec.Builder constructor;
    private String classPackageName;

    public BindObject(Element element) {
        //创建类
        createClass(element);
        //添加字段
        addField(element);
    }

    /**
     * 添加字段
     *
     * @param element 被注解的字段
     */
    public void addField(Element element) {
        String typeName = element.asType().toString();
        String packageName = typeName.substring(0, typeName.lastIndexOf("."));
        String simpleName = typeName.substring(typeName.lastIndexOf(".") + 1);
        String fieldName = element.getSimpleName().toString();

        Bind bind = element.getAnnotation(Bind.class);
        createField(packageName, simpleName, fieldName, bind.value());
    }

    /**
     * 创建字段
     *
     * @param pName     包名
     * @param cName     类名
     * @param fieldName 字段名
     * @param res 资源id
     */
    private void createField(String pName, String cName, String fieldName, int res) {
        ClassName className = ClassName.get(pName, cName);
        FieldSpec fieldSpec = FieldSpec.builder(className, fieldName, Modifier.PUBLIC).build();

        createConstructor(cName, fieldName, res);
        builder.addField(fieldSpec);
    }

    /**
     * 获取类
     */
    public TypeSpec getClassType() {
        builder.addMethod(constructor.build());
        return builder.build();
    }

    /**
     * 返回生成class的包名
     */
    public String getPackageName() {
        return this.classPackageName;
    }

    /**
     * 创建类以及构造函数
     */
    private void createClass(Element element) {
        String totalClassName = element.getEnclosingElement().toString();
        String packageName = totalClassName.substring(0, totalClassName.lastIndexOf("."));
        String simpleClassName = totalClassName.substring(totalClassName.lastIndexOf(".") + 1);
        String className =  simpleClassName + BindProcessor.BINDING;

        classPackageName = packageName;
        //构造类
        builder = TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC);
        //构造函数
        constructor = MethodSpec.constructorBuilder()
                .addParameter(ClassName.get(packageName, simpleClassName), TARGET)
                .addParameter(ClassName.get("android.view", "View"), VIEW)
                .addModifiers(Modifier.PUBLIC);
    }

    /**
     * 在构造函数中添加初始化代码
     */
    private void createConstructor(String cName, String fieldName, int res) {
        constructor.addStatement("$L.$L = ($L) $L.findViewById($L)", TARGET, fieldName, cName, VIEW, res);
    }

}
