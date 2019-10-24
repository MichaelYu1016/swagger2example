package com.example.swaggertest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解Map或String参数的类
 *
 * @author ynx
 * @date 2019-10-24
 * @modified_date 2019-10-24
 */

@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonObject {

    ApiJsonProperty[] value(); //对象属性值

    String name();  //对象名称

    String notes(); // 说明

}

