# 数据脱敏切面

### 使用示例

###### model
```
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
```

###### controller
```
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
```

###### result
```
[
    {
        "id": 1,
        "name": "t*************e",
        "son": {
            "id": 7,
            "name": "n***7"
        },
        "arrStr": [
            "a**1",
            "a**2",
            "a**3"
        ],
        "arrInt": [
            11,
            22,
            33
        ],
        "listStr": [
            "l***1",
            "l***2",
            "l***3"
        ],
        "listSon": null,
        "queueSon": [
            {
                "id": 4,
                "name": "n***4"
            },
            {
                "id": 5,
                "name": "n***5"
            },
            {
                "id": 6,
                "name": "n***6"
            }
        ],
        "mapInt": {
            "11": 11,
            "22": 22,
            "33": 33
        },
        "mapStr": {
            "11": "m**1",
            "22": "m**2",
            "33": "m**3"
        },
        "mapSon": {
            "11": {
                "id": 1,
                "name": "n***1"
            },
            "22": {
                "id": 2,
                "name": "n***2"
            },
            "33": {
                "id": 3,
                "name": "n***3"
            }
        }
    }
]
```