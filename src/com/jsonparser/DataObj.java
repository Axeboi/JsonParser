package com.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class DataObj {
    public String hello;
    // public String phone;
    public List<Object> arrayDataObj = new ArrayList<>();
    public NestedDataObj nestedDataObj;

    public void addArrayDataObj(Object obj) {
        arrayDataObj.add(obj);
    }
}

