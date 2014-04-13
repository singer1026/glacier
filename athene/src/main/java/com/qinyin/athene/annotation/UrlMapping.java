/**
 * @author zhaolie
 * @create-time 2010-11-29
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlMapping {
    String value();

    String type() default "";

    String url() default "";
}
