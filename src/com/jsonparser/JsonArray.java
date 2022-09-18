package com.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class JsonArray {
    List<Object> data = new ArrayList<>();

    public void add(Object obj) {
        this.data.add(obj);
    }

    public Object get(int index) {
        return this.data.get(index);
    }

    public List<Object> getList() {
        return data;
    }

    public int size() {
        return this.data.size();
    }

    public String toString() {
        String str = "[\n";
        //System.out.println("JsonArray size: " + this.size());
        for (Object obj: data) {
            if (obj instanceof JsonObject) {
                JsonObject o = (JsonObject) obj;
                if (o == null) str = str + " ERROR ";
                else str = str + o;
            }
            if (obj instanceof JsonBlock) {
                JsonBlock o = (JsonBlock) obj;
                //System.out.println("HELLO");
                if (o == null) str = str + " ERROR ";
                else str = str + o;
            }

        }
        str = str + "]";
        return str;
    }
}
