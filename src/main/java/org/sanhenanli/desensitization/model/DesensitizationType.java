package org.sanhenanli.desensitization.model;

import java.util.function.Function;

import org.springframework.util.StringUtils;

/**
 * datetime 2021/3/17 上午11:27:30
 * 脱敏类型: 如果脱敏业务不同(例A业务只脱敏姓名,B业务只脱敏手机号), 使用DesensitizationType的不同实现, 在@Desensitize注解中指定types
 *
 * @author alvinfunborn
 */ 
public interface DesensitizationType {
    
    Function<String, String> getDesensitizer();

     /**
     * 姓名脱敏: 中文姓名, 一位不脱敏, 两位保留名, 两位以上保留头尾
     */
    public static String desensitizeName(String s) {
        if(StringUtils.isEmpty(s)) {
            return s;
        }
        char[] cs = s.toCharArray();
        if (cs.length == 2) {
            return "*" + cs[1];
        } else if (cs.length  > 2) {
            for (int i = 1; i < cs.length -1; i++) {
                cs[i] = '*';
            }
            return new String(cs);
        }
        return s;
    }

    /**
     * 手机号脱敏: 手机号隐藏4到7位
     */
    public static String desensitizeMobile(String s) {
        if(StringUtils.isEmpty(s)) {
            return s;
        }
        char[] cs = s.toCharArray();
        if (cs.length < 4) {
            return s;
        } else {
            int index = Math.min(cs.length - 1, 6);
            for (int i = 3; i < index ; i++) {
                cs[i] = '*';
            }
            return new String(cs);
        }
    }
}
