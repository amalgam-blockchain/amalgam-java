package com.tmlab.amalgamj;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

public class CommentOptionsExtension extends StaticVariant {

    public CommentOptionsExtension(List<Beneficiary> beneficiaries) {
        this.id = 0;
        this.params.put("beneficiaries", beneficiaries);
    }

    public static CommentOptionsExtension deserialize(ArrayList<Object> list) {
        return new CommentOptionsExtension((List<Beneficiary>) ((LinkedTreeMap) list.get(1)).get("beneficiaries"));
    }
}
