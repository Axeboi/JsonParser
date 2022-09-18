package com.jsonparser;

public class JsonObject {
    private final String key;
    private final Object value;

    JsonObject(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        if (value instanceof String) {
            return key + ": " + (String) value;
        }
        if (value instanceof JsonArray) {
            return key + ": " + ((JsonArray) value).toString();
        }
        if (value instanceof JsonBlock) {
            System.out.print(key + ": {\n ");
//            return key + "{ " + ((JsonBlock) value).printer() + " }"};
            ((JsonBlock) value).printer();
            return "}";
        }
        return "Unknown";
        //return key + ": Object";
    }
}
