package com.tmlab.amalgamj;

import android.content.Context;
import android.text.TextUtils;

import org.joou.UInteger;
import org.joou.ULong;
import org.joou.UShort;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Amalgam {

    public final static String SORT_ORDER_BY_NAME = "by_name";
    public final static String SORT_ORDER_BY_PROXY = "by_proxy";
    public final static String SORT_ORDER_BY_NEXT_VESTING_WITHDRAWAL = "by_next_vesting_withdrawal";
    public final static String SORT_ORDER_BY_ACCOUNT = "by_account";
    public final static String SORT_ORDER_BY_EXPIRATION = "by_expiration";
    public final static String SORT_ORDER_BY_EFFECTIVE_DATE = "by_effective_date";
    public final static String SORT_ORDER_BY_VOTE_NAME = "by_vote_name";
    public final static String SORT_ORDER_BY_SCHEDULE_TIME = "by_schedule_time";
    public final static String SORT_ORDER_BY_ACCOUNT_WITNESS = "by_account_witness";
    public final static String SORT_ORDER_BY_WITNESS_ACCOUNT = "by_witness_account";
    public final static String SORT_ORDER_BY_FROM_ID = "by_from_id";
    public final static String SORT_ORDER_BY_RATIFICATION_DEADLINE = "by_ratification_deadline";
    public final static String SORT_ORDER_BY_WITHDRAW_ROUTE = "by_withdraw_route";
    public final static String SORT_ORDER_BY_DESTINATION = "by_destination";
    public final static String SORT_ORDER_BY_COMPLETE_FROM_ID = "by_complete_from_id";
    public final static String SORT_ORDER_BY_TO_COMPLETE = "by_to_complete";
    public final static String SORT_ORDER_BY_DELEGATION = "by_delegation";
    public final static String SORT_ORDER_BY_ACCOUNT_EXPIRATION = "by_account_expiration";
    public final static String SORT_ORDER_BY_CONVERSION_DATE = "by_conversion_date";
    public final static String SORT_ORDER_BY_PRICE = "by_price";

    public final static String WITHDRAW_ROUTE_TYPE_INCOMING = "incoming";
    public final static String WITHDRAW_ROUTE_TYPE_OUTGOING = "outgoing";
    public final static String WITHDRAW_ROUTE_TYPE_ALL = "all";

    public final static String BANDWIDTH_TYPE_FORUM = "forum";
    public final static String BANDWIDTH_TYPE_MARKET = "market";

    public interface OnGetBlockHeaderListener {
        void onFinish(Response response, BlockHeader blockHeader);
    }

    public interface OnGetBlockListener {
        void onFinish(Response response, Block block);
    }

    public interface OnGetOperationsListener {
        void onFinish(Response response, AppliedOperation[] operations);
    }

    public interface OnGetAnnotatedTransactionListener {
        void onFinish(Response response, AnnotatedTransaction transaction);
    }

    public interface OnGetConfigListener {
        void onFinish(Response response, LinkedHashMap<String, String> config);
    }

    public interface OnGetVersionListener {
        void onFinish(Response response, Version version);
    }

    public interface OnGetDynamicGlobalPropertiesListener {
        void onFinish(Response response, DynamicGlobalProperties properties);
    }

    public interface OnGetChainPropertiesListener {
        void onFinish(Response response, ChainProperties properties);
    }

    public interface OnGetWitnessScheduleListener {
        void onFinish(Response response, WitnessSchedule witnessSchedule);
    }

    public interface OnGetReserveRatioListener {
        void onFinish(Response response, ReserveRatio reserveRatio);
    }

    public interface OnGetHardforkPropertiesListener {
        void onFinish(Response response, HardforkProperties hardforkProperties);
    }

    public interface OnGetPriceListener {
        void onFinish(Response response, Price price);
    }

    public interface OnGetFeedHistoryListener {
        void onFinish(Response response, FeedHistory feedHistory);
    }

    public interface OnGetWitnessesListener {
        void onFinish(Response response, Witness[] witnesses);
    }

    public interface OnGetWitnessVotesListener {
        void onFinish(Response response, WitnessVote[] witnessVotes);
    }

    public interface OnGetNamesListener {
        void onFinish(Response response, String[] names);
    }

    public interface OnGetAccountsListener {
        void onFinish(Response response, Account[] accounts);
    }

    public interface OnGetAccountHistoryListener {
        void onFinish(Response response, LinkedHashMap<Long, AppliedOperation> operations);
    }

    public interface OnGetAccountBandwidthListener {
        void onFinish(Response response, AccountBandwidth bandwidth);
    }

    public interface OnGetOwnerHistoryListener {
        void onFinish(Response response, OwnerAuthorityHistory[] history);
    }

    public interface OnGetRecoveryRequestsListener {
        void onFinish(Response response, AccountRecoveryRequest[] requests);
    }

    public interface OnGetChangeRecoveryAccountRequestsListener {
        void onFinish(Response response, ChangeRecoveryAccountRequest[] requests);
    }

    public interface OnGetEscrowsListener {
        void onFinish(Response response, Escrow[] escrows);
    }

    public interface OnGetWithdrawVestingRoutesListener {
        void onFinish(Response response, WithdrawVestingRoute[] routes);
    }

    public interface OnGetSavingsWithdrawalsListener {
        void onFinish(Response response, SavingsWithdraw[] withdrawals);
    }

    public interface OnGetVestingDelegationsListener {
        void onFinish(Response response, VestingDelegation[] delegations);
    }

    public interface OnGetVestingDelegationExpirationsListener {
        void onFinish(Response response, VestingDelegationExpiration[] delegations);
    }

    public interface OnGetAbdConversionRequestsListener {
        void onFinish(Response response, ConvertRequest[] requests);
    }

    public interface OnGetDeclineVotingRightsRequestsListener {
        void onFinish(Response response, DeclineVotingRightsRequest[] requests);
    }

    public interface OnGetLimitOrdersListener {
        void onFinish(Response response, LimitOrder[] orders);
    }

    public interface OnGetStringListener {
        void onFinish(Response response, String string);
    }

    public interface OnGetPublicKeysListener {
        void onFinish(Response response, PublicKey[] keys);
    }

    public interface OnGetBooleanListener {
        void onFinish(Response response, Boolean value);
    }

    public interface OnGetKeyReferencesListener {
        void onFinish(Response response, List<String>[] accounts);
    }

    public interface OnResponseListener {
        void onFinish(Response response);
    }

    public interface OnBroadcastResultListener {
        void onFinish(Response response, BroadcastResult result);
    }

    public interface OnGetAccountListener {
        void onFinish(Response response, Account account);
    }

    // Database API

    public static BlockHeader getBlockHeader(UInteger blockNum, final OnGetBlockHeaderListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("block_num", blockNum);
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
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), BlockHeader.class);
        }
        return null;
    }

    public static Block getBlock(UInteger blockNum, final OnGetBlockListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("block_num", blockNum);
        Response response = Connection.execute("database_api", "get_block", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Block block = null;
                if (response.isSuccess()) {
                    block = Serializer.fromJson(response, response.getObject(), Block.class);
                }
                listener.onFinish(response, block);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), Block.class);
        }
        return null;
    }

    public static AppliedOperation[] getOpsInBlock(UInteger blockNum, boolean onlyVirtual, final OnGetOperationsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("block_num", blockNum);
        params.put("only_virtual", onlyVirtual);
        Response response = Connection.execute("database_api", "get_ops_in_block", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AppliedOperation[] operations = null;
                if (response.isSuccess()) {
                    operations = Serializer.fromJson(response, response.getObjectField("ops"), AppliedOperation[].class);
                }
                listener.onFinish(response, operations);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("ops"), AppliedOperation[].class);
        }
        return null;
    }

    public static AnnotatedTransaction getTransaction(Ripemd160 id, final OnGetAnnotatedTransactionListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("id", id);
        Response response = Connection.execute("database_api", "get_transaction", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AnnotatedTransaction transaction = null;
                if (response.isSuccess()) {
                    transaction = Serializer.fromJson(response, response.getObject(), AnnotatedTransaction.class);
                }
                listener.onFinish(response, transaction);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), AnnotatedTransaction.class);
        }
        return null;
    }

    public static LinkedHashMap<String, String> getConfig(final OnGetConfigListener listener) {
        Response response = Connection.execute("database_api", "get_config", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                LinkedHashMap<String, String> config = null;
                if (response.isSuccess()) {
                    config = Serializer.fromJson(response, response.getArray(), LinkedHashMap.class, String.class, String.class);
                }
                listener.onFinish(response, config);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getArray(), LinkedHashMap.class, String.class, String.class);
        }
        return null;
    }

    public static Version getVersion(final OnGetVersionListener listener) {
        Response response = Connection.execute("database_api", "get_version", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Version version = null;
                if (response.isSuccess()) {
                    version = Serializer.fromJson(response, response.getObject(), Version.class);
                }
                listener.onFinish(response, version);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), Version.class);
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
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), DynamicGlobalProperties.class);
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
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), ChainProperties.class);
        }
        return null;
    }

    public static WitnessSchedule getWitnessSchedule(final OnGetWitnessScheduleListener listener) {
        Response response = Connection.execute("database_api", "get_witness_schedule", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                WitnessSchedule witnessSchedule = null;
                if (response.isSuccess()) {
                    witnessSchedule = Serializer.fromJson(response, response.getObject(), WitnessSchedule.class);
                }
                listener.onFinish(response, witnessSchedule);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), WitnessSchedule.class);
        }
        return null;
    }

    public static ReserveRatio getReserveRatio(final OnGetReserveRatioListener listener) {
        Response response = Connection.execute("database_api", "get_reserve_ratio", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ReserveRatio reserveRatio = null;
                if (response.isSuccess()) {
                    reserveRatio = Serializer.fromJson(response, response.getObject(), ReserveRatio.class);
                }
                listener.onFinish(response, reserveRatio);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), ReserveRatio.class);
        }
        return null;
    }

    public static HardforkProperties getHardforkProperties(final OnGetHardforkPropertiesListener listener) {
        Response response = Connection.execute("database_api", "get_hardfork_properties", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                HardforkProperties hardforkProperties = null;
                if (response.isSuccess()) {
                    hardforkProperties = Serializer.fromJson(response, response.getObject(), HardforkProperties.class);
                }
                listener.onFinish(response, hardforkProperties);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), HardforkProperties.class);
        }
        return null;
    }

    public static Price getCurrentPriceFeed(final OnGetPriceListener listener) {
        Response response = Connection.execute("database_api", "get_current_price_feed", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Price price = null;
                if (response.isSuccess()) {
                    price = Serializer.fromJson(response, response.getObject(), Price.class);
                }
                listener.onFinish(response, price);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), Price.class);
        }
        return null;
    }

    public static FeedHistory getFeedHistory(final OnGetFeedHistoryListener listener) {
        Response response = Connection.execute("database_api", "get_feed_history", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                FeedHistory feedHistory = null;
                if (response.isSuccess()) {
                    feedHistory = Serializer.fromJson(response, response.getObject(), FeedHistory.class);
                }
                listener.onFinish(response, feedHistory);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), FeedHistory.class);
        }
        return null;
    }

    public static Witness[] listWitnesses(String start, int limit, String order, final OnGetWitnessesListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_witnesses", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Witness[] witnesses = null;
                if (response.isSuccess()) {
                    witnesses = Serializer.fromJson(response, response.getObjectField("witnesses"), Witness[].class);
                }
                listener.onFinish(response, witnesses);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("witnesses"), Witness[].class);
        }
        return null;
    }

    public static Witness[] findWitnesses(String[] owners, final OnGetWitnessesListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owners", owners);
        Response response = Connection.execute("database_api", "find_witnesses", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Witness[] witnesses = null;
                if (response.isSuccess()) {
                    witnesses = Serializer.fromJson(response, response.getObjectField("witnesses"), Witness[].class);
                }
                listener.onFinish(response, witnesses);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("witnesses"), Witness[].class);
        }
        return null;
    }

    public static WitnessVote[] listWitnessVotes(String start, int limit, String order, final OnGetWitnessVotesListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_witness_votes", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                WitnessVote[] witnessVotes = null;
                if (response.isSuccess()) {
                    witnessVotes = Serializer.fromJson(response, response.getObjectField("votes"), WitnessVote[].class);
                }
                listener.onFinish(response, witnessVotes);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("votes"), WitnessVote[].class);
        }
        return null;
    }

    public static String[] getActiveWitnesses(final OnGetNamesListener listener) {
        Response response = Connection.execute("database_api", "get_active_witnesses", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                String[] names = null;
                if (response.isSuccess()) {
                    names = Serializer.fromJson(response, response.getObjectField("witnesses"), String[].class);
                }
                listener.onFinish(response, names);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("witnesses"), String[].class);
        }
        return null;
    }

    public static Account[] listAccounts(String start, int limit, String order, final OnGetAccountsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_accounts", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Account[] accounts = null;
                if (response.isSuccess()) {
                    accounts = Serializer.fromJson(response, response.getObjectField("accounts"), Account[].class);
                }
                listener.onFinish(response, accounts);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("accounts"), Account[].class);
        }
        return null;
    }

    public static Account[] findAccounts(String[] accounts, final OnGetAccountsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("accounts", accounts);
        Response response = Connection.execute("database_api", "find_accounts", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Account[] accounts = null;
                if (response.isSuccess()) {
                    accounts = Serializer.fromJson(response, response.getObjectField("accounts"), Account[].class);
                }
                listener.onFinish(response, accounts);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("accounts"), Account[].class);
        }
        return null;
    }

    public static LinkedHashMap<Long, AppliedOperation> getAccountHistory(String account, long start, int limit, final OnGetAccountHistoryListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("start", start);
        params.put("limit", limit);
        Response response = Connection.execute("database_api", "get_account_history", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                LinkedHashMap<Long, AppliedOperation> operations = null;
                if (response.isSuccess()) {
                    operations = Serializer.fromJson(response, response.getObjectField("history"), LinkedHashMap.class, Long.class, AppliedOperation.class);
                }
                listener.onFinish(response, operations);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("history"), LinkedHashMap.class, Long.class, AppliedOperation.class);
        }
        return null;
    }

    public static AccountBandwidth getAccountBandwidth(String account, String type, final OnGetAccountBandwidthListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("type", type);
        Response response = Connection.execute("database_api", "get_account_bandwidth", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AccountBandwidth bandwidth = null;
                if (response.isSuccess()) {
                    bandwidth = Serializer.fromJson(response, response.getObjectField("bandwidth"), AccountBandwidth.class);
                }
                listener.onFinish(response, bandwidth);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("bandwidth"), AccountBandwidth.class);
        }
        return null;
    }

    public static OwnerAuthorityHistory[] listOwnerHistories(String start, int limit, final OnGetOwnerHistoryListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        Response response = Connection.execute("database_api", "list_owner_histories", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                OwnerAuthorityHistory[] history = null;
                if (response.isSuccess()) {
                    history = Serializer.fromJson(response, response.getObjectField("owner_auths"), OwnerAuthorityHistory[].class);
                }
                listener.onFinish(response, history);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("owner_auths"), OwnerAuthorityHistory[].class);
        }
        return null;
    }

    public static OwnerAuthorityHistory[] findOwnerHistories(String owner, final OnGetOwnerHistoryListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owner", owner);
        Response response = Connection.execute("database_api", "find_owner_histories", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                OwnerAuthorityHistory[] history = null;
                if (response.isSuccess()) {
                    history = Serializer.fromJson(response, response.getObjectField("owner_auths"), OwnerAuthorityHistory[].class);
                }
                listener.onFinish(response, history);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("owner_auths"), OwnerAuthorityHistory[].class);
        }
        return null;
    }

    public static AccountRecoveryRequest[] listAccountRecoveryRequests(String start, int limit, String order, final OnGetRecoveryRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_account_recovery_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AccountRecoveryRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), AccountRecoveryRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), AccountRecoveryRequest[].class);
        }
        return null;
    }

    public static AccountRecoveryRequest[] findAccountRecoveryRequests(String[] accounts, final OnGetRecoveryRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("accounts", accounts);
        Response response = Connection.execute("database_api", "find_account_recovery_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AccountRecoveryRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), AccountRecoveryRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), AccountRecoveryRequest[].class);
        }
        return null;
    }

    public static ChangeRecoveryAccountRequest[] listChangeRecoveryAccountRequests(String start, int limit, String order, final OnGetChangeRecoveryAccountRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_change_recovery_account_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ChangeRecoveryAccountRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), ChangeRecoveryAccountRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), ChangeRecoveryAccountRequest[].class);
        }
        return null;
    }

    public static ChangeRecoveryAccountRequest[] findChangeRecoveryAccountRequests(String[] accounts, final OnGetChangeRecoveryAccountRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("accounts", accounts);
        Response response = Connection.execute("database_api", "find_change_recovery_account_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ChangeRecoveryAccountRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), ChangeRecoveryAccountRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), ChangeRecoveryAccountRequest[].class);
        }
        return null;
    }

    public static Escrow[] listEscrows(String start, int limit, String order, final OnGetEscrowsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_escrows", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Escrow[] escrows = null;
                if (response.isSuccess()) {
                    escrows = Serializer.fromJson(response, response.getObjectField("escrows"), Escrow[].class);
                }
                listener.onFinish(response, escrows);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("escrows"), Escrow[].class);
        }
        return null;
    }

    public static Escrow[] findEscrows(String from, final OnGetEscrowsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        Response response = Connection.execute("database_api", "find_escrows", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Escrow[] escrows = null;
                if (response.isSuccess()) {
                    escrows = Serializer.fromJson(response, response.getObjectField("escrows"), Escrow[].class);
                }
                listener.onFinish(response, escrows);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("escrows"), Escrow[].class);
        }
        return null;
    }

    public static WithdrawVestingRoute[] listWithdrawVestingRoutes(String start, int limit, String order, final OnGetWithdrawVestingRoutesListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_withdraw_vesting_routes", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                WithdrawVestingRoute[] routes = null;
                if (response.isSuccess()) {
                    routes = Serializer.fromJson(response, response.getObjectField("routes"), WithdrawVestingRoute[].class);
                }
                listener.onFinish(response, routes);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("routes"), WithdrawVestingRoute[].class);
        }
        return null;
    }

    public static WithdrawVestingRoute[] findWithdrawVestingRoutes(String account, String order, final OnGetWithdrawVestingRoutesListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("order", order);
        Response response = Connection.execute("database_api", "find_withdraw_vesting_routes", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                WithdrawVestingRoute[] routes = null;
                if (response.isSuccess()) {
                    routes = Serializer.fromJson(response, response.getObjectField("routes"), WithdrawVestingRoute[].class);
                }
                listener.onFinish(response, routes);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("routes"), WithdrawVestingRoute[].class);
        }
        return null;
    }

    public static SavingsWithdraw[] listSavingsWithdrawals(String start, int limit, String order, final OnGetSavingsWithdrawalsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_savings_withdrawals", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                SavingsWithdraw[] withdrawals = null;
                if (response.isSuccess()) {
                    withdrawals = Serializer.fromJson(response, response.getObjectField("withdrawals"), SavingsWithdraw[].class);
                }
                listener.onFinish(response, withdrawals);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("withdrawals"), SavingsWithdraw[].class);
        }
        return null;
    }

    public static SavingsWithdraw[] findSavingsWithdrawals(String account, final OnGetSavingsWithdrawalsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        Response response = Connection.execute("database_api", "find_savings_withdrawals", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                SavingsWithdraw[] withdrawals = null;
                if (response.isSuccess()) {
                    withdrawals = Serializer.fromJson(response, response.getObjectField("withdrawals"), SavingsWithdraw[].class);
                }
                listener.onFinish(response, withdrawals);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("withdrawals"), SavingsWithdraw[].class);
        }
        return null;
    }

    public static VestingDelegation[] listVestingDelegations(String start, int limit, String order, final OnGetVestingDelegationsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_vesting_delegations", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                VestingDelegation[] delegations = null;
                if (response.isSuccess()) {
                    delegations = Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegation[].class);
                }
                listener.onFinish(response, delegations);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegation[].class);
        }
        return null;
    }

    public static VestingDelegation[] findVestingDelegations(String account, final OnGetVestingDelegationsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        Response response = Connection.execute("database_api", "find_vesting_delegations", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                VestingDelegation[] delegations = null;
                if (response.isSuccess()) {
                    delegations = Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegation[].class);
                }
                listener.onFinish(response, delegations);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegation[].class);
        }
        return null;
    }

    public static VestingDelegationExpiration[] listVestingDelegationExpirations(String start, int limit, String order, final OnGetVestingDelegationExpirationsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_vesting_delegation_expirations", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                VestingDelegationExpiration[] delegations = null;
                if (response.isSuccess()) {
                    delegations = Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegationExpiration[].class);
                }
                listener.onFinish(response, delegations);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegationExpiration[].class);
        }
        return null;
    }

    public static VestingDelegationExpiration[] findVestingDelegationExpirations(String account, final OnGetVestingDelegationExpirationsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        Response response = Connection.execute("database_api", "find_vesting_delegation_expirations", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                VestingDelegationExpiration[] delegations = null;
                if (response.isSuccess()) {
                    delegations = Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegationExpiration[].class);
                }
                listener.onFinish(response, delegations);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("delegations"), VestingDelegationExpiration[].class);
        }
        return null;
    }

    public static ConvertRequest[] listAbdConversionRequests(String start, int limit, String order, final OnGetAbdConversionRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_abd_conversion_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ConvertRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), ConvertRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), ConvertRequest[].class);
        }
        return null;
    }

    public static ConvertRequest[] findAbdConversionRequests(String account, final OnGetAbdConversionRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        Response response = Connection.execute("database_api", "find_abd_conversion_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ConvertRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), ConvertRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), ConvertRequest[].class);
        }
        return null;
    }

    public static DeclineVotingRightsRequest[] listDeclineVotingRightsRequests(String start, int limit, String order, final OnGetDeclineVotingRightsRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_decline_voting_rights_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                DeclineVotingRightsRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), DeclineVotingRightsRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), DeclineVotingRightsRequest[].class);
        }
        return null;
    }

    public static DeclineVotingRightsRequest[] findDeclineVotingRightsRequests(String[] accounts, final OnGetDeclineVotingRightsRequestsListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("accounts", accounts);
        Response response = Connection.execute("database_api", "find_decline_voting_rights_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                DeclineVotingRightsRequest[] requests = null;
                if (response.isSuccess()) {
                    requests = Serializer.fromJson(response, response.getObjectField("requests"), DeclineVotingRightsRequest[].class);
                }
                listener.onFinish(response, requests);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("requests"), DeclineVotingRightsRequest[].class);
        }
        return null;
    }

    public static LimitOrder[] listLimitOrders(String start, int limit, String order, final OnGetLimitOrdersListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        params.put("order", order);
        Response response = Connection.execute("database_api", "list_limit_orders", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                LimitOrder[] orders = null;
                if (response.isSuccess()) {
                    orders = Serializer.fromJson(response, response.getObjectField("orders"), LimitOrder[].class);
                }
                listener.onFinish(response, orders);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("orders"), LimitOrder[].class);
        }
        return null;
    }

    public static LimitOrder[] findLimitOrders(String account, final OnGetLimitOrdersListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        Response response = Connection.execute("database_api", "find_limit_orders", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                LimitOrder[] orders = null;
                if (response.isSuccess()) {
                    orders = Serializer.fromJson(response, response.getObjectField("orders"), LimitOrder[].class);
                }
                listener.onFinish(response, orders);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("orders"), LimitOrder[].class);
        }
        return null;
    }

    public static String getTransactionHex(Transaction trx, final OnGetStringListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("trx", trx);
        Response response = Connection.execute("database_api", "get_transaction_hex", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                String string = null;
                if (response.isSuccess()) {
                    string = Serializer.fromJson(response, response.getObjectField("hex"), String.class);
                }
                listener.onFinish(response, string);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("hex"), String.class);
        }
        return null;
    }

    public static PublicKey[] getRequiredSignatures(Transaction trx, PublicKey[] availableKeys, final OnGetPublicKeysListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("trx", trx);
        params.put("available_keys", availableKeys);
        Response response = Connection.execute("database_api", "get_required_signatures", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                PublicKey[] keys = null;
                if (response.isSuccess()) {
                    keys = Serializer.fromJson(response, response.getObjectField("keys"), PublicKey[].class);
                }
                listener.onFinish(response, keys);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("keys"), PublicKey[].class);
        }
        return null;
    }

    public static PublicKey[] getPotentialSignatures(Transaction trx, final OnGetPublicKeysListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("trx", trx);
        Response response = Connection.execute("database_api", "get_potential_signatures", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                PublicKey[] keys = null;
                if (response.isSuccess()) {
                    keys = Serializer.fromJson(response, response.getObjectField("keys"), PublicKey[].class);
                }
                listener.onFinish(response, keys);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("keys"), PublicKey[].class);
        }
        return null;
    }

    public static Boolean verifyAuthority(Transaction trx, final OnGetBooleanListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("trx", trx);
        Response response = Connection.execute("database_api", "verify_authority", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Boolean value = null;
                if (response.isSuccess()) {
                    value = Serializer.fromJson(response, response.getObjectField("valid"), Boolean.class);
                }
                listener.onFinish(response, value);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("valid"), Boolean.class);
        }
        return null;
    }

    public static Boolean verifyAccountAuthority(String account, PublicKey[] signers, final OnGetBooleanListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("signers", signers);
        Response response = Connection.execute("database_api", "verify_account_authority", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Boolean value = null;
                if (response.isSuccess()) {
                    value = Serializer.fromJson(response, response.getObjectField("valid"), Boolean.class);
                }
                listener.onFinish(response, value);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("valid"), Boolean.class);
        }
        return null;
    }

    public static Boolean verifySignatures(String hash, Signature[] signatures, String[] requiredOwner,
                                           String[] requiredActive, String[] requiredPosting, Authority[] requiredOther,
                                           final OnGetBooleanListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("hash", hash);
        params.put("signatures", signatures);
        params.put("required_owner", requiredOwner);
        params.put("required_active", requiredActive);
        params.put("required_posting", requiredPosting);
        params.put("required_other", requiredOther);
        Response response = Connection.execute("database_api", "verify_signatures", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Boolean value = null;
                if (response.isSuccess()) {
                    value = Serializer.fromJson(response, response.getObjectField("valid"), Boolean.class);
                }
                listener.onFinish(response, value);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("valid"), Boolean.class);
        }
        return null;
    }

    // Account By Key API

    public static List<String>[] getKeyReferences(PublicKey[] keys, final OnGetKeyReferencesListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("keys", keys);
        Response response = Connection.execute("account_by_key_api", "get_key_references", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                List<String>[] accounts = null;
                if (response.isSuccess()) {
                    accounts = Serializer.fromJson(response, response.getObjectField("accounts"), List[].class, String.class);
                }
                listener.onFinish(response, accounts);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObjectField("accounts"), List[].class, String.class);
        }
        return null;
    }

    // Network broadcast API

    public static void broadcastTransaction(Transaction trx, final OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("trx", trx);
        Connection.execute("network_broadcast_api", "broadcast_transaction", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                listener.onFinish(response);
            }
        });
    }

    public static BroadcastResult broadcastTransactionSynchronous(Transaction trx, final OnBroadcastResultListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("trx", trx);
        Response response = Connection.execute("network_broadcast_api", "broadcast_transaction_synchronous", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                BroadcastResult result = null;
                if (response.isSuccess()) {
                    result = Serializer.fromJson(response, response.getObject(), BroadcastResult.class);
                }
                listener.onFinish(response, result);
            }
        });
        if ((response != null) && response.isSuccess() && (listener == null)) {
            return Serializer.fromJson(response, response.getObject(), BroadcastResult.class);
        }
        return null;
    }

    public static void broadcastBlock(SignedBlock block, final OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("block", block);
        Connection.execute("network_broadcast_api", "broadcast_block", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                listener.onFinish(response);
            }
        });
    }

    // Operations

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

    public static Response limitOrderCreate(PrivateKey privateKey, String owner, UInteger orderId, Asset amountToSell,
                                            Asset minToReceive, boolean fillOrKill, TimePointSec expiration,
                                            boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owner", owner);
        params.put("orderid", orderId);
        params.put("amount_to_sell", amountToSell);
        params.put("min_to_receive", minToReceive);
        params.put("fill_or_kill", fillOrKill);
        params.put("expiration", expiration);
        return Connection.broadcast(privateKey, "limit_order_create", params, prepare, listener);
    }

    public static Response limitOrderCancel(PrivateKey privateKey, String owner, UInteger orderId,
                                            boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owner", owner);
        params.put("orderid", orderId);
        return Connection.broadcast(privateKey, "limit_order_cancel", params, prepare, listener);
    }

    public static Response feedPublish(PrivateKey privateKey, String publisher, Price exchangeRate,
                                       boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("publisher", publisher);
        params.put("exchange_rate", exchangeRate);
        return Connection.broadcast(privateKey, "feed_publish", params, prepare, listener);
    }

    public static Response convert(PrivateKey privateKey, String owner, UInteger requestId, Asset amount,
                                   boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owner", owner);
        params.put("requestid", requestId);
        params.put("amount", amount);
        return Connection.broadcast(privateKey, "convert", params, prepare, listener);
    }

    public static Response accountCreate(PrivateKey privateKey, Asset fee, String creator, String newAccountName,
                                         Authority owner, Authority active, Authority posting, PublicKey memoKey,
                                         String jsonMetadata, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("fee", fee);
        params.put("creator", creator);
        params.put("new_account_name", newAccountName);
        params.put("owner", owner);
        params.put("active", active);
        params.put("posting", posting);
        params.put("memo_key", memoKey);
        params.put("json_metadata", jsonMetadata);
        return Connection.broadcast(privateKey, "account_create", params, prepare, listener);
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

    public static Response witnessUpdate(PrivateKey privateKey, String owner, String url, PublicKey blockSigningKey,
                                         ChainProperties props, Asset fee, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owner", owner);
        params.put("url", url);
        params.put("block_signing_key", blockSigningKey);
        params.put("props", props);
        params.put("fee", fee);
        return Connection.broadcast(privateKey, "witness_update", params, prepare, listener);
    }

    public static Response accountWitnessVote(PrivateKey privateKey, String account, String witness, boolean approve,
                                              boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("witness", witness);
        params.put("approve", approve);
        return Connection.broadcast(privateKey, "account_witness_vote", params, prepare, listener);
    }

    public static Response accountWitnessProxy(PrivateKey privateKey, String account, String proxy,
                                               boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("proxy", proxy);
        return Connection.broadcast(privateKey, "account_witness_proxy", params, prepare, listener);
    }

    public static Response setWithdrawVestingRoute(PrivateKey privateKey, String fromAccount, String toAccount,
                                                   UShort percent, boolean autoVest, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from_account", fromAccount);
        params.put("to_account", toAccount);
        params.put("percent", percent);
        params.put("auto_vest", autoVest);
        return Connection.broadcast(privateKey, "set_withdraw_vesting_route", params, prepare, listener);
    }

    public static Response limitOrderCreate2(PrivateKey privateKey, String owner, UInteger orderId, Asset amountToSell,
                                             Price exchangeRate, boolean fillOrKill, TimePointSec expiration,
                                             boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owner", owner);
        params.put("orderid", orderId);
        params.put("amount_to_sell", amountToSell);
        params.put("exchange_rate", exchangeRate);
        params.put("fill_or_kill", fillOrKill);
        params.put("expiration", expiration);
        return Connection.broadcast(privateKey, "limit_order_create2", params, prepare, listener);
    }

    public static Response requestAccountRecovery(PrivateKey privateKey, String recoveryAccount, String accountToRecover,
                                                  Authority newOwnerAuthority, List<FutureExtensions> extensions,
                                                  boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("recovery_account", recoveryAccount);
        params.put("account_to_recover", accountToRecover);
        params.put("new_owner_authority", newOwnerAuthority);
        params.put("extensions", extensions);
        return Connection.broadcast(privateKey, "request_account_recovery", params, prepare, listener);
    }

    public static Response recoverAccount(PrivateKey privateKey, String accountToRecover, Authority newOwnerAuthority,
                                          Authority recentOwnerAuthority, List<FutureExtensions> extensions,
                                          boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account_to_recover", accountToRecover);
        params.put("new_owner_authority", newOwnerAuthority);
        params.put("recent_owner_authority", recentOwnerAuthority);
        params.put("extensions", extensions);
        return Connection.broadcast(privateKey, "recover_account", params, prepare, listener);
    }

    public static Response changeRecoveryAccount(PrivateKey privateKey, String accountToRecover, String newRecoveryAccount,
                                                 List<FutureExtensions> extensions, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account_to_recover", accountToRecover);
        params.put("new_recovery_account", newRecoveryAccount);
        params.put("extensions", extensions);
        return Connection.broadcast(privateKey, "change_recovery_account", params, prepare, listener);
    }

    public static Response escrowTransfer(PrivateKey privateKey, String from, String to, Asset amountABD, Asset amountAmalgam,
                                          UInteger escrowId, String agent, Asset fee, String jsonMeta,
                                          TimePointSec ratificationDeadline, TimePointSec escrowExpiration,
                                          boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("abd_amount", amountABD);
        params.put("amalgam_amount", amountAmalgam);
        params.put("escrow_id", escrowId);
        params.put("agent", agent);
        params.put("fee", fee);
        params.put("json_meta", jsonMeta);
        params.put("ratification_deadline", ratificationDeadline);
        params.put("escrow_expiration", escrowExpiration);
        return Connection.broadcast(privateKey, "escrow_transfer", params, prepare, listener);
    }

    public static Response escrowDispute(PrivateKey privateKey, String from, String to, String agent, String who,
                                         UInteger escrowId, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("agent", agent);
        params.put("who", who);
        params.put("escrow_id", escrowId);
        return Connection.broadcast(privateKey, "escrow_dispute", params, prepare, listener);
    }

    public static Response escrowRelease(PrivateKey privateKey, String from, String to, String agent, String who,
                                         String receiver, UInteger escrowId, Asset amountABD, Asset amountAmalgam,
                                         boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("agent", agent);
        params.put("who", who);
        params.put("receiver", receiver);
        params.put("escrow_id", escrowId);
        params.put("abd_amount", amountABD);
        params.put("amalgam_amount", amountAmalgam);
        return Connection.broadcast(privateKey, "escrow_release", params, prepare, listener);
    }

    public static Response escrowApprove(PrivateKey privateKey, String from, String to, String agent, String who,
                                         UInteger escrowId, boolean approve, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("agent", agent);
        params.put("who", who);
        params.put("escrow_id", escrowId);
        params.put("approve", approve);
        return Connection.broadcast(privateKey, "escrow_approve", params, prepare, listener);
    }

    public static Response transferToSavings(PrivateKey privateKey, String from, String to, Asset amount, String memo,
                                             boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("amount", amount);
        params.put("memo", memo);
        return Connection.broadcast(privateKey, "transfer_to_savings", params, prepare, listener);
    }

    public static Response transferFromSavings(PrivateKey privateKey, String from, UInteger requestId,
                                               String to, Asset amount, String memo,
                                               boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("request_id", requestId);
        params.put("to", to);
        params.put("amount", amount);
        params.put("memo", memo);
        return Connection.broadcast(privateKey, "transfer_from_savings", params, prepare, listener);
    }

    public static Response cancelTransferFromSavings(PrivateKey privateKey, String from, UInteger requestId,
                                                     boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("request_id", requestId);
        return Connection.broadcast(privateKey, "cancel_transfer_from_savings", params, prepare, listener);
    }

    public static Response declineVotingRights(PrivateKey privateKey, String account, boolean decline,
                                               boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("decline", decline);
        return Connection.broadcast(privateKey, "decline_voting_rights", params, prepare, listener);
    }

    public static Response delegateVestingShares(PrivateKey privateKey, String delegator, String delegatee, Asset vestingShares,
                                                 boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("delegator", delegator);
        params.put("delegatee", delegatee);
        params.put("vesting_shares", vestingShares);
        return Connection.broadcast(privateKey, "delegate_vesting_shares", params, prepare, listener);
    }

    public static Response witnessSetProperties(PrivateKey privateKey, String owner, List<String> props, List<FutureExtensions> extensions,
                                                boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("owner", owner);
        params.put("props", props);
        params.put("extensions", extensions);
        return Connection.broadcast(privateKey, "witness_set_properties", params, prepare, listener);
    }

    // Helper functions

    public static Account getAccount(String name, final OnGetAccountListener listener) {
        Account[] accounts = findAccounts(new String[]{name}, listener == null ? null : new OnGetAccountsListener() {
            @Override
            public void onFinish(Response response, Account[] accounts) {
                Account account = null;
                if (response.isSuccess()) {
                    if (accounts.length > 0) {
                        account = accounts[0];
                    } else {
                        response.setNotFound();
                    }
                }
                listener.onFinish(response, account);
            }
        });
        if ((accounts != null) && (accounts.length > 0) && (listener == null)) {
            return accounts[0];
        }
        return null;
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
            if (!label.matches("^[a-z].*")) {
                return context.getString(resId, context.getString(R.string.error_start_with_a_letter));
            }
            if (!label.matches("^[a-z0-9-]*")) {
                return context.getString(resId, context.getString(R.string.error_have_only_letters_digits_or_dashes));
            }
            if (label.contains("--")) {
                return context.getString(resId, context.getString(R.string.error_have_only_one_dash_in_a_row));
            }
            if (!label.matches("^.*[a-z0-9]")) {
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

    public static Response updateAccount(PrivateKey privateKey, String account, PublicKey ownerKey, PublicKey activeKey, PublicKey postingKey,
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
}
