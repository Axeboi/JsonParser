package com.jsonparser;

import java.lang.reflect.Array;
import java.util.*;

public class JsonParser {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException {


        // String json = "{\"hello\": \"world\", \"arrayDataObj\": [{\"key\": \"firstValue\"}, {\"key\": \"secondValue\"}], \"nestedDataObj\": {\"newHello\": \"newWorld\"} } ";
        // String json = "{\"hello\": \"world\", \"nestedDataObj\": {\"newHello\": \"newWorld\"}, \"arrayDataObj\": [{\"key\": \"firstValue\"}, {\"key\": \"secondValue\"}] } ";

        String json = """
        {
            "hello": "world",
            "arrayDataObj": [
                {
                    "key": "firstValue"
                },
                {
                    "key": "secondValue"
                }
            ],
            "nestedDataObj": {
                "createClass": {
                    "instanceParam": "newWorld"
                }
            }
        }
        """;

//        String json = """
//                    { "myKey": "myValue" }
//                """;
        System.out.println("Raw Json:\n" + json);
        System.out.println();
        JsonScanner scanner = new JsonScanner(json);
        List<Token> tokens = scanner.scan();

        Parser parser = new Parser(tokens);
        System.out.println("Parsed data:");
        parser.parse();

        List<String> skipKeys = new ArrayList<>();
        skipKeys.add("nestedDataObj");

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("myKey", "NestedDataObj");
        mapParams.put("instanceParam", "newHello");

        ClassFromJson classFromJson = new ClassFromJson(parser.getAstTree(), "com.jsonparser.NestedDataObj", skipKeys, mapParams);

        // JsonInterpreter interpreter = new JsonInterpreter(parser.getAstTree());

        try {
            NestedDataObj res = (NestedDataObj) classFromJson.interpret(classFromJson.astTree);
//            System.out.println("Result: " + res.newHello);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        try {
//            DataObj res = (DataObj) interpreter.interpret("com.jsonparser.DataObj");
//            System.out.println(res.hello);
//            System.out.println(res.nestedDataObj.newHello);
//            System.out.println(res.arrayDataObj);
//            ArrayDataObj obj1 = (ArrayDataObj) res.arrayDataObj.get(0);
//            ArrayDataObj obj2 = (ArrayDataObj) res.arrayDataObj.get(1);
//            System.out.println(obj1.key);
//            System.out.println(obj2.key);
//        } catch (ClassNotFoundException e) {
//            System.out.println(e);
//        } catch (IllegalAccessException e) {
//            System.out.println(e);
//        }



        //System.out.println(parser.getData().size());

//        for (Object obj: parser.getData()) {
//            if (obj instanceof JsonObject) {
//                System.out.println("JsonObject");
//                System.out.println((JsonObject) obj);
//            }
//            if (obj instanceof JsonArray) {
//                System.out.println("JsonArray");
//                List<Object> arr = ((JsonArray) obj).getList();
//                for (Object o : arr) {
//                    JsonObject jsonObj = (JsonObject) o;
//                    System.out.println(jsonObj);
//                }
//            }
//        }
    }
}
