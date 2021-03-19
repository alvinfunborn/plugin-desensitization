package org.sanhenanli.desensitization.advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sanhenanli.desensitization.model.DesensitizationType;

/**
 * datetime 2021/3/17 下午2:48:22
 *
 * @author alvinfunborn
 */ 
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitize {
    
    /**
     * 使用的types域
     */
    Class<?> types() default DesensitizationType.class;
}
