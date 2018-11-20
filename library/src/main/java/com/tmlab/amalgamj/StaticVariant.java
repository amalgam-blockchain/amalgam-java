package com.tmlab.amalgamj;

import java.util.LinkedHashMap;

public class StaticVariant {

    protected int id;

    public LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    public int getId() {
        return id;
    }

    public LinkedHashMap<String, Object> serialize() {
        LinkedHashMap<String, Object> list = new LinkedHashMap<>();
        list.put("type", getId());
        list.put("value", params);
        return list;
    }
}
