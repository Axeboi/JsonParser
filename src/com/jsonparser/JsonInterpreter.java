package com.jsonparser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class JsonInterpreter {

    private List<Object> objects;
    private Object astTree;

    JsonInterpreter(Object astTree) { this.astTree = astTree; }

    public Object interpret(String schemaClassName) throws IllegalAccessException, ClassNotFoundException {
        Object dataClass = initInstanceByClassName(schemaClassName);
        try {
            return setDataWithClassSchema(dataClass, ((JsonBlock) astTree).getBlock());
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        }
        return null;
    }

    private Object setDataWithClassSchema(Object dataClass, List<Object> currentObjects) throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException {

        Field[] fields = dataClass.getClass().getFields();
        for (Field field: fields) {
            String name = field.getName();

            for (Object obj: currentObjects) {
                if (obj instanceof JsonObject && ((JsonObject) obj).getKey().equals(name)) {
                    Object value = ((JsonObject) obj).getValue();
                    if (value instanceof String) {
                        field.set(dataClass, value);
                    }

                    if (value instanceof JsonArray) {
                        // Field name already exist and can be assumed to have a List
                        // loop through the elements and add to List

                        // create instance of field
                        String arrayClassName = "com.jsonparser." + capitalize(name);
                        List<Object> list = new ArrayList<>();

                        for (Object o: ((JsonArray) value).getList()) {
                            List<Object> sendObject = new ArrayList<>();
                            sendObject.add(o);
                            Object arrayClassInstance = initInstanceByClassName(arrayClassName);
                            list.add(setDataWithClassSchema(arrayClassInstance, sendObject));
                        }
                        field.set(dataClass, list);
                    }

                    if (value instanceof JsonBlock) {
                        System.out.println(name);

                        String nestedClassName = "com.jsonparser." + capitalize(name);
                        Object nestedClassInstance = initInstanceByClassName(nestedClassName);
                        field.set(dataClass, setDataWithClassSchema(nestedClassInstance, ((JsonBlock) value).getBlock()));
                    }
                }
                else if (obj instanceof JsonBlock) {
                    for(Object o : ((JsonBlock) obj).getBlock()) {
                        if (((JsonObject) o).getKey().equals(name)) {
                            field.set(dataClass, ((JsonObject) o).getValue());
                        }
                    }
                }
            }
        }
        return dataClass;
    }

    private Object initInstanceByClassName(String className) {
        Object data = null;
        try {
            Class c = Class.forName(className);
            data = c.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            System.exit(60);
        } catch (InstantiationException e) {
            System.out.println(e);
            System.exit(60);
        } catch (IllegalAccessException e) {
            System.out.println(e);
            System.exit(60);
        } catch (NoSuchMethodException e) {
            System.out.println(e);
            System.exit(60);
        } catch (InvocationTargetException e) {
            System.out.println(e);
            System.exit(60);
        }
        return data;
    }

    private String capitalize(String str) {
        if (str == null || str.length() == 0) return str;
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
