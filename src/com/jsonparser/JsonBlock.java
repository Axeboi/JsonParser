package com.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class JsonBlock {
    private List<Object> block = new ArrayList<>();

//    JsonBlock(Object block) {
//        this.block = block;
//    }

    public List<Object> getBlock() {
        return block;
    }

    public void add(Object obj) {
        this.block.add(obj);
    }

    public String toString() {
        String str = "";
        for(Object obj: block) {
            if (obj instanceof JsonObject) {
                str = str + (JsonObject) obj + "\n";
            }
        }
        return str;
    }
    public void printer() {
        for (Object obj: block) {
            if (obj instanceof JsonObject) {
                System.out.println((JsonObject) obj);
            }
            if (obj instanceof JsonArray) {
                List<Object> arr = ((JsonArray) obj).getList();
                for (Object o : arr) {
                    System.out.println("  JsonObject");
                    if (o == null) System.out.println("  NULL");
                    else System.out.println(o);
                }
            }
            if (obj instanceof JsonBlock) {
                ((JsonBlock) obj).printer();
            }
            //System.out.println(obj);
        }
    }
}
