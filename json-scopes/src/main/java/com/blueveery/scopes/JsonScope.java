package com.blueveery.scopes;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tomek on 23.09.16.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonScope {
    boolean positive();
    Class<?>[] scope() default { };
    boolean lazyLoadBorders() default true;
}
