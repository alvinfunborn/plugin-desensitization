package org.sanhenanli.desensitization.model.enums;

import java.util.function.Function;

import org.sanhenanli.desensitization.model.DesensitizationType;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2021/3/17 上午11:19:50
 * 脱敏类型
 *
 * @author alvinfunborn
 */ 
@AllArgsConstructor
@Getter
public enum BasicDesensitizationTypeEnum implements DesensitizationType {
    
    /**
     * 姓名
     */
    NAME(DesensitizationType::desensitizeName),

    /**
     * 手机号
     */
    MOBILE(DesensitizationType::desensitizeMobile),

    ;

    /**
     * 脱敏方法
     */
    private Function<String, String> desensitizer;
}
