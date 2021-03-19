package org.sanhenanli.desensitization.advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sanhenanli.desensitization.model.enums.BasicDesensitizationTypeEnum;


/**
 * datetime 2021/3/17 上午11:17:34
 * 脱敏注解: 注解在成员变量上
 *
 * @author alvinfunborn
 */ 
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizeAs {
    
    /**
     * 脱敏类型
     */
    BasicDesensitizationTypeEnum value() default BasicDesensitizationTypeEnum.NAME;
}
