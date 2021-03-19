package org.sanhenanli.desensitization.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.sanhenanli.desensitization.advice.Desensitize;
import org.sanhenanli.desensitization.model.TestModel;
import org.sanhenanli.desensitization.model.TestModel.Son;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * datetime 2021/3/17 下午4:29:45
 *
 * @author alvinfunborn
 */ 
@RestController
@RequestMapping("/test/desensitization")
public class TestController {
    
    @Desensitize
    @GetMapping("")
    public List<TestModel> test() {
        List<Son> sons = new ArrayList<Son>(10);
        for (int i = 0; i < 10; i++) {
            sons.add(new Son(i, "name" + i));
        }
        TestModel model = new TestModel();
        model.setId(1);
        model.setArrInt(new int[] {11, 22, 33});
        model.setArrStr(new String[] {"arr1", "arr2", "arr3"});
        List<String> listStr = new ArrayList<String>();
        listStr.add("list1");
        listStr.add("list2");
        listStr.add("list3");
        model.setListStr(listStr);
        Map<String, Integer> mapInt = new HashMap<>();
        mapInt.put("11", 11);
        mapInt.put("22", 22);
        mapInt.put("33", 33);
        model.setMapInt(mapInt);
        Map<String, String> mapStr = new HashMap<>();
        mapStr.put("11", "map1");
        mapStr.put("22", "map2");
        mapStr.put("33", "map3");
        model.setMapStr(mapStr);
        Map<String, Son> mapSon = new HashMap<>();
        mapSon.put("11", sons.get(1));
        mapSon.put("22", sons.get(2));
        mapSon.put("33", sons.get(3));
        model.setMapSon(mapSon);
        model.setName("this is my name");
        Queue<Son> queue = new LinkedBlockingDeque<>();
        queue.add(sons.get(4));
        queue.add(sons.get(5));
        queue.add(sons.get(6));
        model.setQueueSon(queue);
        model.setSon(sons.get(7));

        return Collections.singletonList(model);
    }
}
