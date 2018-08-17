package com.tmlab.amalgamj;

import android.content.Context;
import android.text.TextUtils;

import org.joou.UInteger;
import org.joou.UShort;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Amalgam {

    public interface OnGetBlockHeaderListener {
        void onFinish(Response response, BlockHeader blockHeader);
    }

    public interface OnGetChainPropertiesListener {
        void onFinish(Response response, ChainProperties properties);
    }

    public interface OnGetDynamicGlobalPropertiesListener {
        void onFinish(Response response, DynamicGlobalProperties properties);
    }

    public interface OnGetAccountListener {
        void onFinish(Response response, Account account);
    }

    public interface OnGetAccountHistoryListener {
        void onFinish(Response response, LinkedHashMap<Long, AppliedOperation> operations);
    }

    public interface OnGetContent {
        void onFinish(Response response, Discussion discussion);
    }

    public interface OnGetPrice {
        void onFinish(Response response, Price price);
    }

    public static String validateAccountName(Context context, String value) {
        int resId = R.string.error_account_name_should;
        if (TextUtils.isEmpty(value)) {
            return context.getString(resId, context.getString(R.string.error_not_be_empty));
        }
        if (value.length() < 3) {
            return context.getString(resId, context.getString(R.string.error_be_longer));
        }
        if (value.length() > 16) {
            return context.getString(resId, context.getString(R.string.error_be_shorter));
        }
        if (value.contains(".")) {
            resId = R.string.error_each_account_segment_should;
        }
        String[] ref = TextUtils.split(value, "\\.");
        for (String label : ref) {
            if (!label.matches("^[a-z].*$")) {
                return context.getString(resId, context.getString(R.string.error_start_with_a_letter));
            }
            if (!label.matches("^[a-z0-9-]*$")) {
                return context.getString(resId, context.getString(R.string.error_have_only_letters_digits_or_dashes));
            }
            if (label.contains("--")) {
                return context.getString(resId, context.getString(R.string.error_have_only_one_dash_in_a_row));
            }
            if (!label.matches("^.*[a-z0-9]$")) {
                return context.getString(resId, context.getString(R.string.error_end_with_a_letter_or_digit));
            }
            if (!(label.length() >= 3)) {
                return context.getString(resId, context.getString(R.string.error_be_longer));
            }
        }
        return null;
    }

    public static PrivateKey generateKey(String name, String password, String role) {
        String seed = name + role + password;
        String brainKey = seed.trim();
        byte[] bytes = null;
        try {
            bytes = brainKey.getBytes("UTF-8");
        } catch (Exception e) {
            //
        }
        return new PrivateKey(Utils.bigIntFromBuffer(Utils.hashSha256(bytes)));
    }

    public static BlockHeader getBlockHeader(UInteger blockNum, final OnGetBlockHeaderListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(blockNum);
        Response response = Connection.execute("database_api", "get_block_header", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                BlockHeader blockHeader = null;
                if (response.isSuccess()) {
                    blockHeader = Serializer.fromJson(response, response.getObject(), BlockHeader.class);
                }
                listener.onFinish(response, blockHeader);
            }
        });
        if ((response != null) && (listener == null)) {
            BlockHeader blockHeader = null;
            if (response.isSuccess()) {
                blockHeader = Serializer.fromJson(response, response.getObject(), BlockHeader.class);
            }
            return blockHeader;
        }
        return null;
    }

    public static ChainProperties getChainProperties(final OnGetChainPropertiesListener listener) {
        Response response = Connection.execute("database_api", "get_chain_properties", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ChainProperties properties = null;
                if (response.isSuccess()) {
                    properties = Serializer.fromJson(response, response.getObject(), ChainProperties.class);
                }
                listener.onFinish(response, properties);
            }
        });
        if ((response != null) && (listener == null)) {
            ChainProperties properties = null;
            if (response.isSuccess()) {
                properties = Serializer.fromJson(response, response.getObject(), ChainProperties.class);
            }
            return properties;
        }
        return null;
    }

    public static DynamicGlobalProperties getDynamicGlobalProperties(final OnGetDynamicGlobalPropertiesListener listener) {
        Response response = Connection.execute("database_api", "get_dynamic_global_properties", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                DynamicGlobalProperties properties = null;
                if (response.isSuccess()) {
                    properties = Serializer.fromJson(response, response.getObject(), DynamicGlobalProperties.class);
                }
                listener.onFinish(response, properties);
            }
        });
        if ((response != null) && (listener == null)) {
            DynamicGlobalProperties properties = null;
            if (response.isSuccess()) {
                properties = Serializer.fromJson(response, response.getObject(), DynamicGlobalProperties.class);
            }
            return properties;
        }
        return null;
    }

    public static Account getAccount(String name, final OnGetAccountListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(new String[]{name});
        Response response = Connection.execute("database_api", "get_accounts", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Account account = null;
                if (response.isSuccess()) {
                    Account[] accounts = Serializer.fromJson(response, response.getArray(), Account[].class);
                    if ((accounts != null) && (accounts.length > 0)) {
                        account = accounts[0];
                    } else {
                        response.setNotFound();
                    }
                }
                listener.onFinish(response, account);
            }
        });
        if ((response != null) && (listener == null)) {
            Account account = null;
            if (response.isSuccess()) {
                Account[] accounts = Serializer.fromJson(response, response.getArray(), Account[].class);
                if ((accounts != null) && (accounts.length > 0)) {
                    account = accounts[0];
                } else {
                    response.setNotFound();
                }
            }
            return account;
        }
        return null;
    }

    public static LinkedHashMap<Long, AppliedOperation> getAccountHistory(String name, long from, int limit, final OnGetAccountHistoryListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(name);
        params.add(from);
        params.add(limit);
        Response response = Connection.execute("database_api", "get_account_history", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                LinkedHashMap<Long, AppliedOperation> operations = null;
                if (response.isSuccess()) {
                    operations = Serializer.fromJson(response, response.getArray(), LinkedHashMap.class, Long.class, AppliedOperation.class);
                }
                listener.onFinish(response, operations);
            }
        });
        if ((response != null) && (listener == null)) {
            LinkedHashMap<Long, AppliedOperation> operations = null;
            if (response.isSuccess()) {
                operations = Serializer.fromJson(response, response.getArray(), LinkedHashMap.class, Long.class, AppliedOperation.class);
            }
            return operations;
        }
        return null;
    }

    public static Discussion getContent(String author, String permlink, final OnGetContent listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(author);
        params.add(permlink);
        Response response = Connection.execute("database_api", "get_content", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Discussion discussion = null;
                if (response.isSuccess()) {
                    discussion = Serializer.fromJson(response, response.getObject(), Discussion.class);
                }
                listener.onFinish(response, discussion);
            }
        });
        if ((response != null) && (listener == null)) {
            Discussion discussion = null;
            if (response.isSuccess()) {
                discussion = Serializer.fromJson(response, response.getObject(), Discussion.class);
            }
            return discussion;
        }
        return null;
    }

    public static Price getCurrentMedianHistoryPrice(final OnGetPrice listener) {
        Response response = Connection.execute("database_api", "get_current_median_history_price", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Price price = null;
                if (response.isSuccess()) {
                    price = Serializer.fromJson(response, response.getObject(), Price.class);
                }
                listener.onFinish(response, price);
            }
        });
        if ((response != null) && (listener == null)) {
            Price price = null;
            if (response.isSuccess()) {
                price = Serializer.fromJson(response, response.getObject(), Price.class);
            }
            return price;
        }
        return null;
    }

    public static Response accountUpdate(PrivateKey privateKey, String account, Authority owner, Authority active, Authority posting,
                                         PublicKey memoKey, String jsonMetadata, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("owner", new Optional(owner));
        params.put("active", new Optional(active));
        params.put("posting", new Optional(posting));
        params.put("memo_key", memoKey);
        params.put("json_metadata", jsonMetadata);
        return Connection.broadcast(privateKey, "account_update", params, prepare, listener);
    }

    public static Response accountUpdate(PrivateKey privateKey, String account, PublicKey ownerKey, PublicKey activeKey, PublicKey postingKey,
                                         PublicKey memoKey, String jsonMetadata, boolean prepare, Connection.OnResponseListener listener) {
        Authority owner = null;
        if (ownerKey != null) {
            owner = new Authority();
            owner.weight_threshold = UInteger.valueOf(1);
            owner.key_auths.put(ownerKey, UShort.valueOf(1));
        }
        Authority active = null;
        if (activeKey != null) {
            active = new Authority();
            active.weight_threshold = UInteger.valueOf(1);
            active.key_auths.put(activeKey, UShort.valueOf(1));
        }
        Authority posting = null;
        if (postingKey != null) {
            posting = new Authority();
            posting.weight_threshold = UInteger.valueOf(1);
            posting.key_auths.put(postingKey, UShort.valueOf(1));
        }
        return accountUpdate(privateKey, account, owner, active, posting, memoKey, jsonMetadata, prepare, listener);
    }

    public static Response vote(PrivateKey privateKey, String voter, String author, String permlink, UShort weight,
                                boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("voter", voter);
        params.put("author", author);
        params.put("permlink", permlink);
        params.put("weight", weight);
        return Connection.broadcast(privateKey, "vote", params, prepare, listener);
    }

    public static Response comment(PrivateKey privateKey, String parentAuthor, String parentPermlink, String author, String permlink,
                                   String title, String body, String jsonMetadata, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("parent_author", parentAuthor);
        params.put("parent_permlink", parentPermlink);
        params.put("author", author);
        params.put("permlink", permlink);
        params.put("title", title);
        params.put("body", body);
        params.put("json_metadata", jsonMetadata);
        return Connection.broadcast(privateKey, "comment", params, prepare, listener);
    }

    public static Response transfer(PrivateKey privateKey, String from, String to, Asset amount, String memo,
                                    boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("amount", amount);
        params.put("memo", memo);
        return Connection.broadcast(privateKey, "transfer", params, prepare, listener);
    }

    public static Response transferToVesting(PrivateKey privateKey, String from, String to, Asset amount,
                                             boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("amount", amount);
        return Connection.broadcast(privateKey, "transfer_to_vesting", params, prepare, listener);
    }

    public static Response withdrawVesting(PrivateKey privateKey, String account, Asset vestingShares,
                                           boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("vesting_shares", vestingShares);
        return Connection.broadcast(privateKey, "withdraw_vesting", params, prepare, listener);
    }

    private static Response feedPublish(PrivateKey privateKey, String publisher, Price exchangeRate,
                                        boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("publisher", publisher);
        params.put("exchange_rate", exchangeRate);
        return Connection.broadcast(privateKey, "feed_publish", params, prepare, listener);
    }

    public static Response claimRewardBalance(PrivateKey privateKey, String account, Asset rewardAmalgam, Asset rewardABD, Asset rewardVests,
                                              boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("reward_amalgam", rewardAmalgam);
        params.put("reward_abd", rewardABD);
        params.put("reward_vests", rewardVests);
        return Connection.broadcast(privateKey, "claim_reward_balance", params, prepare, listener);
    }
}
