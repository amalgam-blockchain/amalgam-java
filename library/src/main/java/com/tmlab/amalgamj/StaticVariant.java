package com.tmlab.amalgamj;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StaticVariant {

    protected int id;

    public LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    public int getId() {
        return id;
    }

    public ArrayList<Object> serialize() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(getId());
        list.add(params);
        return list;
    }
}
