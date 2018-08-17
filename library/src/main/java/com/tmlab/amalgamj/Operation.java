package com.tmlab.amalgamj;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Operation {

    public String name;
    public LinkedHashMap<String, Object> params;

    private static final List<String> OPERATIONS = Arrays.asList(
        "vote",
        "comment",
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
        "pow",
        "custom",
        "report_over_production",
        "delete_comment",
        "custom_json",
        "comment_options",
        "set_withdraw_vesting_route",
        "limit_order_create2",
        "challenge_authority",
        "prove_authority",
        "request_account_recovery",
        "recover_account",
        "change_recovery_account",
        "escrow_transfer",
        "escrow_dispute",
        "escrow_release",
        "pow2",
        "escrow_approve",
        "transfer_to_savings",
        "transfer_from_savings",
        "cancel_transfer_from_savings",
        "custom_binary",
        "decline_voting_rights",
        "reset_account",
        "set_reset_account",
        "claim_reward_balance",
        "delegate_vesting_shares",
        "account_create_with_delegation",
        "fill_convert_request",
        "author_reward",
        "curation_reward",
        "comment_reward",
        "liquidity_reward",
        "interest",
        "fill_vesting_withdraw",
        "fill_order",
        "shutdown_witness",
        "fill_transfer_from_savings",
        "hardfork",
        "comment_payout_update",
        "return_vesting_delegation",
        "comment_benefactor_reward",
        "producer_reward"
    );

    public Operation(String name, LinkedHashMap<String, Object> params) {
        this.name = name;
        this.params = params;
    }

    public int getId() {
        return OPERATIONS.indexOf(name);
    }
}
