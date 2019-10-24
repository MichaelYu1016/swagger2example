package com.example.swaggertest.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解Map或String参数的属性
 *
 * @author ynx
 * @date 2019-10-24
 * @modified_date 2019-10-24
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonProperty {

    String key();  //key

    String example() default "";

    String type() default "string";  //支持string, int, double(需要更多类型可在MapApiReader.java中增加参数类型)

    String description() default "";

    boolean required() default false; // 是否必填

}
