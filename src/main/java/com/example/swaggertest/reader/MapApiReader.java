package com.example.swaggertest.reader;

import com.example.swaggertest.annotation.ApiJsonObject;
import com.example.swaggertest.annotation.ApiJsonProperty;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.Map;

/**
 * 设置Swagger读取Map参数的插件
 *
 * @author ynx
 * @date 2019-10-24
 * @modified_date 2019-10-24
 */
@Component
@Order   //plugin加载顺序，默认是最后加载
public class MapApiReader implements ParameterBuilderPlugin {
    @Autowired
    private TypeResolver typeResolver;

    private final static String basePackage = "com.example.swaggertest.model.";  //动态生成的Class名

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();

        if (methodParameter.getParameterType().canCreateSubtype(Map.class) || methodParameter.getParameterType().canCreateSubtype(String.class)) { //判断是否需要修改对象ModelRef,这里我判断的是Map类型和String类型需要重新修改ModelRef对象
            Optional<ApiJsonObject> optional = methodParameter.findAnnotation(ApiJsonObject.class);  //根据参数上的ApiJsonObject注解中的参数动态生成Class
            if (optional.isPresent()) {
                String name = optional.get().name();  //model 名称
                String notes = optional.get().notes(); // model 的描述
                ApiJsonProperty[] properties = optional.get().value();

                parameterContext.getDocumentationContext().getAdditionalModels().add(typeResolver.resolve(createRefModel(properties, name)));  //像documentContext的Models中添加我们新生成的Class

                parameterContext.parameterBuilder()  //修改Map参数的ModelRef为我们动态生成的class
                        .parameterType("body")
                        .modelRef(new ModelRef(name))
                        .description(notes)
                        .name(name);
            }
        }

    }

    /**
     * 根据properties中的值动态生成含有Swagger注解的javaBean
     */
    private Class createRefModel(ApiJsonProperty[] properties, String name) {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(basePackage + name);

        try {
            for (ApiJsonProperty property : properties) {
                ctClass.addField(createField(property, ctClass));
            }
            return ctClass.toClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据property的值生成含有swagger apiModelProperty注解的属性
     */
    private CtField createField(ApiJsonProperty property, CtClass ctClass) throws NotFoundException, CannotCompileException {
        CtField ctField = new CtField(getFieldType(property.type()), property.key(), ctClass);
        ctField.setModifiers(Modifier.PUBLIC);

        ConstPool constPool = ctClass.getClassFile().getConstPool();

        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation ann = new Annotation("io.swagger.annotations.ApiModelProperty", constPool);
        // 开始向注解增加属性值
        ann.addMemberValue("value", new StringMemberValue(property.description(), constPool));
        // 根据type的类型设置ApiModelProperty的dataType属性，目前可支持string，integer，double，可根据需要增加.
        if (ctField.getType().subclassOf(ClassPool.getDefault().get(String.class.getName()))) {
            ann.addMemberValue("dataType", new StringMemberValue("string", constPool));
        }
        if (ctField.getType().subclassOf(ClassPool.getDefault().get(Integer.class.getName()))) {
            ann.addMemberValue("dataType", new StringMemberValue("integer",constPool));
        }
        if (ctField.getType().subclassOf(ClassPool.getDefault().get(Double.class.getName()))) {
            ann.addMemberValue("dataType",new StringMemberValue("double",constPool));
        }
        // 设置是否必填
        if (property.required()) {
            ann.addMemberValue("required", new BooleanMemberValue(true,constPool));
        }else {
            ann.addMemberValue("required", new BooleanMemberValue(false,constPool));
        }

        ann.addMemberValue("example",new StringMemberValue(property.example(),constPool));
        attr.addAnnotation(ann);
        ctField.getFieldInfo().addAttribute(attr);

        return ctField;
    }

    private CtClass getFieldType(String type) throws NotFoundException {
        CtClass fileType = null;
        switch (type) {
            case "string":
                fileType = ClassPool.getDefault().get(String.class.getName());
                break;
            case "int":
                fileType = ClassPool.getDefault().get(Integer.class.getName());
                break;
            case "double":
                fileType = ClassPool.getDefault().get(Double.class.getName());
                break;
        }
        return fileType;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}

