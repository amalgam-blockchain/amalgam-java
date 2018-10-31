package com.tmlab.amalgamj;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Operation extends StaticVariant {

    public String name;

    private static final List<String> OPERATIONS = Arrays.asList(
        "transfer",
        "transfer_to_vesting",
        "withdraw_vesting",
        "limit_order_create",
        "limit_order_cancel",
        "feed_publish",
        "convert",
        "account_create",
        "account_update",
        "witness_update",
        "account_witness_vote",
        "account_witness_proxy",
        "custom",
        "custom_json",
        "set_withdraw_vesting_route",
        "limit_order_create2",
        "request_account_recovery",
        "recover_account",
        "change_recovery_account",
        "escrow_transfer",
        "escrow_dispute",
        "escrow_release",
        "escrow_approve",
        "transfer_to_savings",
        "transfer_from_savings",
        "cancel_transfer_from_savings",
        "custom_binary",
        "decline_voting_rights",
        "delegate_vesting_shares",
        "tbd1",
        "tbd2",
        "tbd3",
        "tbd4",
        "tbd5",
        "tbd6",
        "tbd7",
        "tbd8",
        "tbd9",
        "tbd10",
        "fill_convert_request",
        "interest",
        "fill_vesting_withdraw",
        "fill_order",
        "shutdown_witness",
        "fill_transfer_from_savings",
        "hardfork",
        "return_vesting_delegation",
        "producer_reward"
    );

    public Operation(String name, LinkedHashMap<String, Object> params) {
        this.name = name;
        this.params = params;
    }

    @Override
    public int getId() {
        return OPERATIONS.indexOf(name);
    }

    public static Operation deserialize(ArrayList<Object> list) {
        LinkedTreeMap<String, Object> map = (LinkedTreeMap) list.get(1);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            params.put(item.getKey(), item.getValue());
        }
        return new Operation((String) list.get(0), params);
    }

    @Override
    public ArrayList<Object> serialize() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(name);
        list.add(params);
        return list;
    }
}
