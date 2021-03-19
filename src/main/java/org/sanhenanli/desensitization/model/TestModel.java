package org.sanhenanli.desensitization.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.sanhenanli.desensitization.advice.DesensitizeAs;
import org.sanhenanli.desensitization.model.enums.BasicDesensitizationTypeEnum;

import lombok.Data;

/**
 * datetime 2021/3/17 下午4:18:34
 *
 * @author alvinfunborn
 */
@Data
public class TestModel {
    
    private int id;
    @DesensitizeAs(BasicDesensitizationTypeEnum.NAME)
    private String name;
    private Son son;
    @DesensitizeAs(BasicDesensitizationTypeEnum.NAME)
    private String[] arrStr;
    private int[] arrInt;
    @DesensitizeAs(BasicDesensitizationTypeEnum.NAME)
    private List<String> listStr;
    private List<Son> listSon;
    private Queue<Son> queueSon;
    private Map<String, Integer> mapInt;
    @DesensitizeAs(BasicDesensitizationTypeEnum.NAME)
    private Map<String, String> mapStr;
    private Map<String, Son> mapSon;

    public static class Son implements Serializable {
        private static final long serialVersionUID = 7765340045906075803L;
        private int id;
        @DesensitizeAs(BasicDesensitizationTypeEnum.NAME)
        private String name;
        public Son(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }
}
