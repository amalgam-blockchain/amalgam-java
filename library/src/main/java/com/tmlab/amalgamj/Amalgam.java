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

    public final static int WITHDRAW_ROUTE_TYPE_INCOMING = 0;
    public final static int WITHDRAW_ROUTE_TYPE_OUTGOING = 1;
    public final static int WITHDRAW_ROUTE_TYPE_ALL = 2;

    public final static int BANDWIDTH_TYPE_POST = 0;
    public final static int BANDWIDTH_TYPE_FORUM = 1;
    public final static int BANDWIDTH_TYPE_MARKET = 2;

    public interface OnGetBlockHeaderListener {
        void onFinish(Response response, BlockHeader blockHeader);
    }

    public interface OnGetBlockListener {
        void onFinish(Response response, Block block);
    }

    public interface OnGetOperationsListener {
        void onFinish(Response response, AppliedOperation[] operations);
    }

    public interface OnGetConfigListener {
        void onFinish(Response response, LinkedHashMap<String, String> config);
    }

    public interface OnGetDynamicGlobalPropertiesListener {
        void onFinish(Response response, DynamicGlobalProperties properties);
    }

    public interface OnGetChainPropertiesListener {
        void onFinish(Response response, ChainProperties properties);
    }

    public interface OnGetFeedHistoryListener {
        void onFinish(Response response, FeedHistory feedHistory);
    }

    public interface OnGetPriceListener {
        void onFinish(Response response, Price price);
    }

    public interface OnGetWitnessScheduleListener {
        void onFinish(Response response, WitnessSchedule witnessSchedule);
    }

    public interface OnGetStringListener {
        void onFinish(Response response, String string);
    }

    public interface OnGetScheduledHardforkListener {
        void onFinish(Response response, ScheduledHardfork scheduledHardfork);
    }

    public interface OnGetKeyReferencesListener {
        void onFinish(Response response, List<String>[] accounts);
    }

    public interface OnGetExtendedAccountsListener {
        void onFinish(Response response, ExtendedAccount[] accounts);
    }

    public interface OnGetAccountsListener {
        void onFinish(Response response, Account[] accounts);
    }

    public interface OnGetNamesListener {
        void onFinish(Response response, String[] names);
    }

    public interface OnGetCountListener {
        void onFinish(Response response, ULong count);
    }

    public interface OnGetConvertRequestsListener {
        void onFinish(Response response, ConvertRequest[] convertRequests);
    }

    public interface OnGetAccountHistoryListener {
        void onFinish(Response response, LinkedHashMap<Long, AppliedOperation> operations);
    }

    public interface OnGetOwnerHistoryListener {
        void onFinish(Response response, OwnerAuthorityHistory[] history);
    }

    public interface OnGetRecoveryRequestListener {
        void onFinish(Response response, AccountRecoveryRequest request);
    }

    public interface OnGetEscrowListener {
        void onFinish(Response response, Escrow escrow);
    }

    public interface OnGetWithdrawRoutesListener {
        void onFinish(Response response, WithdrawRoute[] routes);
    }

    public interface OnGetAccountBandwidthListener {
        void onFinish(Response response, AccountBandwidth bandwidth);
    }

    public interface OnGetSavingsWithdrawListener {
        void onFinish(Response response, SavingsWithdraw[] withdraw);
    }

    public interface OnGetOrderBookListener {
        void onFinish(Response response, OrderBook orderBook);
    }

    public interface OnGetOrdersListener {
        void onFinish(Response response, ExtendedLimitOrder[] orders);
    }

    public interface OnGetLiquidityQueueListener {
        void onFinish(Response response, LiquidityBalance[] queue);
    }

    public interface OnGetAnnotatedTransactionListener {
        void onFinish(Response response, AnnotatedTransaction transaction);
    }

    public interface OnGetPublicKeysListener {
        void onFinish(Response response, PublicKey[] keys);
    }

    public interface OnGetBooleanListener {
        void onFinish(Response response, Boolean value);
    }

    public interface OnGetActiveVotesListener {
        void onFinish(Response response, VoteState[] votes);
    }

    public interface OnGetAccountVotesListener {
        void onFinish(Response response, AccountVote[] votes);
    }

    public interface OnGetContentListener {
        void onFinish(Response response, Discussion discussion);
    }

    public interface OnGetContentRepliesListener {
        void onFinish(Response response, Discussion[] replies);
    }

    public interface OnGetExtendedAccountListener {
        void onFinish(Response response, ExtendedAccount account);
    }

    // Database API

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
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), BlockHeader.class);
            }
        }
        return null;
    }

    public static Block getBlock(UInteger blockNum, final OnGetBlockListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(blockNum);
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
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), Block.class);
            }
        }
        return null;
    }

    public static AppliedOperation[] getOpsInBlock(UInteger blockNum, boolean onlyVirtual, final OnGetOperationsListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(blockNum);
        params.add(onlyVirtual);
        Response response = Connection.execute("database_api", "get_ops_in_block", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AppliedOperation[] operations = null;
                if (response.isSuccess()) {
                    operations = Serializer.fromJson(response, response.getArray(), AppliedOperation[].class);
                }
                listener.onFinish(response, operations);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), AppliedOperation[].class);
            }
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
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), LinkedHashMap.class, String.class, String.class);
            }
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
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), DynamicGlobalProperties.class);
            }
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
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), ChainProperties.class);
            }
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
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), FeedHistory.class);
            }
        }
        return null;
    }

    public static Price getCurrentMedianHistoryPrice(final OnGetPriceListener listener) {
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
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), Price.class);
            }
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
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), WitnessSchedule.class);
            }
        }
        return null;
    }

    public static String getHardforkVersion(final OnGetStringListener listener) {
        Response response = Connection.execute("database_api", "get_hardfork_version", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                String string = null;
                if (response.isSuccess()) {
                    string = Serializer.fromJson(response, response.getObject(), String.class);
                }
                listener.onFinish(response, string);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), String.class);
            }
        }
        return null;
    }

    public static ScheduledHardfork getNextScheduledHardfork(final OnGetScheduledHardforkListener listener) {
        Response response = Connection.execute("database_api", "get_next_scheduled_hardfork", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ScheduledHardfork scheduledHardfork = null;
                if (response.isSuccess()) {
                    scheduledHardfork = Serializer.fromJson(response, response.getObject(), ScheduledHardfork.class);
                }
                listener.onFinish(response, scheduledHardfork);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), ScheduledHardfork.class);
            }
        }
        return null;
    }

    public static List<String>[] getKeyReferences(PublicKey[] keys, final OnGetKeyReferencesListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(keys);
        Response response = Connection.execute("account_by_key_api", "get_key_references", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                List<String>[] accounts = null;
                if (response.isSuccess()) {
                    accounts = Serializer.fromJson(response, response.getArray(), List[].class, String.class);
                }
                listener.onFinish(response, accounts);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), List[].class, String.class);
            }
        }
        return null;
    }

    public static ExtendedAccount[] getAccounts(String[] names, final OnGetExtendedAccountsListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(names);
        Response response = Connection.execute("database_api", "get_accounts", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ExtendedAccount[] accounts = null;
                if (response.isSuccess()) {
                    accounts = Serializer.fromJson(response, response.getArray(), ExtendedAccount[].class);
                }
                listener.onFinish(response, accounts);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), ExtendedAccount[].class);
            }
        }
        return null;
    }

    public static ExtendedAccount[] lookupAccountNames(String[] accountNames, final OnGetAccountsListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(accountNames);
        Response response = Connection.execute("database_api", "lookup_account_names", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ExtendedAccount[] accounts = null;
                if (response.isSuccess()) {
                    accounts = Serializer.fromJson(response, response.getArray(), ExtendedAccount[].class);
                }
                listener.onFinish(response, accounts);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), ExtendedAccount[].class);
            }
        }
        return null;
    }

    public static String[] lookupAccounts(String lowerBoundName, int limit, final OnGetNamesListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(lowerBoundName);
        params.add(limit);
        Response response = Connection.execute("database_api", "lookup_accounts", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                String[] names = null;
                if (response.isSuccess()) {
                    names = Serializer.fromJson(response, response.getArray(), String[].class);
                }
                listener.onFinish(response, names);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), String[].class);
            }
        }
        return null;
    }

    public static ULong getAccountCount(final OnGetCountListener listener) {
        Response response = Connection.execute("database_api", "get_account_count", null, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ULong count = null;
                if (response.isSuccess()) {
                    count = Serializer.fromJson(response, response.getObject(), ULong.class);
                }
                listener.onFinish(response, count);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), ULong.class);
            }
        }
        return null;
    }

    public static ConvertRequest[] getConversionRequests(String accountName, final OnGetConvertRequestsListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(accountName);
        Response response = Connection.execute("database_api", "get_conversion_requests", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ConvertRequest[] convertRequests = null;
                if (response.isSuccess()) {
                    convertRequests = Serializer.fromJson(response, response.getArray(), ConvertRequest[].class);
                }
                listener.onFinish(response, convertRequests);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), ConvertRequest[].class);
            }
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
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), LinkedHashMap.class, Long.class, AppliedOperation.class);
            }
        }
        return null;
    }

    public static OwnerAuthorityHistory[] getOwnerHistory(String account, final OnGetOwnerHistoryListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(account);
        Response response = Connection.execute("database_api", "get_owner_history", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                OwnerAuthorityHistory[] history = null;
                if (response.isSuccess()) {
                    history = Serializer.fromJson(response, response.getArray(), OwnerAuthorityHistory[].class);
                }
                listener.onFinish(response, history);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), OwnerAuthorityHistory[].class);
            }
        }
        return null;
    }

    public static AccountRecoveryRequest getRecoveryRequest(String account, final OnGetRecoveryRequestListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(account);
        Response response = Connection.execute("database_api", "get_recovery_request", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AccountRecoveryRequest request = null;
                if (response.isSuccess()) {
                    request = Serializer.fromJson(response, response.getObject(), AccountRecoveryRequest.class);
                }
                listener.onFinish(response, request);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), AccountRecoveryRequest.class);
            }
        }
        return null;
    }

    public static Escrow getEscrow(String from, UInteger escrowId, final OnGetEscrowListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(from);
        params.add(escrowId);
        Response response = Connection.execute("database_api", "get_escrow", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Escrow escrow = null;
                if (response.isSuccess()) {
                    escrow = Serializer.fromJson(response, response.getObject(), Escrow.class);
                }
                listener.onFinish(response, escrow);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), Escrow.class);
            }
        }
        return null;
    }

    public static WithdrawRoute[] getWithdrawRoutes(String account, int withdrawRouteType, final OnGetWithdrawRoutesListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(account);
        params.add(withdrawRouteType);
        Response response = Connection.execute("database_api", "get_withdraw_routes", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                WithdrawRoute[] routes = null;
                if (response.isSuccess()) {
                    routes = Serializer.fromJson(response, response.getArray(), WithdrawRoute[].class);
                }
                listener.onFinish(response, routes);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), WithdrawRoute[].class);
            }
        }
        return null;
    }

    public static AccountBandwidth getAccountBandwidth(String account, int bandwidthType, final OnGetAccountBandwidthListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(account);
        params.add(bandwidthType);
        Response response = Connection.execute("database_api", "get_account_bandwidth", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AccountBandwidth bandwidth = null;
                if (response.isSuccess()) {
                    bandwidth = Serializer.fromJson(response, response.getObject(), AccountBandwidth.class);
                }
                listener.onFinish(response, bandwidth);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), AccountBandwidth.class);
            }
        }
        return null;
    }

    public static SavingsWithdraw[] getSavingsWithdrawFrom(String account, final OnGetSavingsWithdrawListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(account);
        Response response = Connection.execute("database_api", "get_savings_withdraw_from", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                SavingsWithdraw[] withdraw = null;
                if (response.isSuccess()) {
                    withdraw = Serializer.fromJson(response, response.getArray(), SavingsWithdraw[].class);
                }
                listener.onFinish(response, withdraw);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), SavingsWithdraw[].class);
            }
        }
        return null;
    }

    public static SavingsWithdraw[] getSavingsWithdrawTo(String account, final OnGetSavingsWithdrawListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(account);
        Response response = Connection.execute("database_api", "get_savings_withdraw_to", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                SavingsWithdraw[] withdraw = null;
                if (response.isSuccess()) {
                    withdraw = Serializer.fromJson(response, response.getArray(), SavingsWithdraw[].class);
                }
                listener.onFinish(response, withdraw);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), SavingsWithdraw[].class);
            }
        }
        return null;
    }

    public static OrderBook getOrderBook(int limit, final OnGetOrderBookListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(limit);
        Response response = Connection.execute("database_api", "get_order_book", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                OrderBook orderBook = null;
                if (response.isSuccess()) {
                    orderBook = Serializer.fromJson(response, response.getObject(), OrderBook.class);
                }
                listener.onFinish(response, orderBook);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), OrderBook.class);
            }
        }
        return null;
    }

    public static ExtendedLimitOrder[] getOpenOrders(String owner, final OnGetOrdersListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(owner);
        Response response = Connection.execute("database_api", "get_open_orders", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                ExtendedLimitOrder[] orders = null;
                if (response.isSuccess()) {
                    orders = Serializer.fromJson(response, response.getArray(), ExtendedLimitOrder[].class);
                }
                listener.onFinish(response, orders);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), ExtendedLimitOrder[].class);
            }
        }
        return null;
    }

    public static LiquidityBalance[] getLiquidityQueue(String startAccount, int limit, final OnGetLiquidityQueueListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(startAccount);
        params.add(limit);
        Response response = Connection.execute("database_api", "get_liquidity_queue", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                LiquidityBalance[] queue = null;
                if (response.isSuccess()) {
                    queue = Serializer.fromJson(response, response.getArray(), LiquidityBalance[].class);
                }
                listener.onFinish(response, queue);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), LiquidityBalance[].class);
            }
        }
        return null;
    }

    public static String getTransactionHex(Transaction trx, final OnGetStringListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(trx);
        Response response = Connection.execute("database_api", "get_transaction_hex", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                String string = null;
                if (response.isSuccess()) {
                    string = Serializer.fromJson(response, response.getObject(), String.class);
                }
                listener.onFinish(response, string);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), String.class);
            }
        }
        return null;
    }

    public static AnnotatedTransaction getTransaction(Ripemd160 trxId, final OnGetAnnotatedTransactionListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(trxId);
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
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), AnnotatedTransaction.class);
            }
        }
        return null;
    }

    public static PublicKey[] getRequiredSignatures(Transaction trx, PublicKey[] availableKeys, final OnGetPublicKeysListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(trx);
        params.add(availableKeys);
        Response response = Connection.execute("database_api", "get_required_signatures", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                PublicKey[] keys = null;
                if (response.isSuccess()) {
                    keys = Serializer.fromJson(response, response.getArray(), PublicKey[].class);
                }
                listener.onFinish(response, keys);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), PublicKey[].class);
            }
        }
        return null;
    }

    public static PublicKey[] getPotentialSignatures(Transaction trx, final OnGetPublicKeysListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(trx);
        Response response = Connection.execute("database_api", "get_potential_signatures", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                PublicKey[] keys = null;
                if (response.isSuccess()) {
                    keys = Serializer.fromJson(response, response.getArray(), PublicKey[].class);
                }
                listener.onFinish(response, keys);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), PublicKey[].class);
            }
        }
        return null;
    }

    public static Boolean verifyAuthority(Transaction trx, final OnGetBooleanListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(trx);
        Response response = Connection.execute("database_api", "verify_authority", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Boolean value = null;
                if (response.isSuccess()) {
                    value = Serializer.fromJson(response, response.getObject(), Boolean.class);
                }
                listener.onFinish(response, value);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), Boolean.class);
            }
        }
        return null;
    }

    public static Boolean verifyAccountAuthority(String nameOrId, PublicKey[] signers, final OnGetBooleanListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(nameOrId);
        params.add(signers);
        Response response = Connection.execute("database_api", "verify_account_authority", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Boolean value = null;
                if (response.isSuccess()) {
                    value = Serializer.fromJson(response, response.getObject(), Boolean.class);
                }
                listener.onFinish(response, value);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), Boolean.class);
            }
        }
        return null;
    }

    public static VoteState[] getActiveVotes(String permlink, final OnGetActiveVotesListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(permlink);
        Response response = Connection.execute("database_api", "get_active_votes", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                VoteState[] votes = null;
                if (response.isSuccess()) {
                    votes = Serializer.fromJson(response, response.getArray(), VoteState[].class);
                }
                listener.onFinish(response, votes);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), VoteState[].class);
            }
        }
        return null;
    }

    public static AccountVote[] getAccountVotes(String voter, final OnGetAccountVotesListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(voter);
        Response response = Connection.execute("database_api", "get_account_votes", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                AccountVote[] votes = null;
                if (response.isSuccess()) {
                    votes = Serializer.fromJson(response, response.getArray(), AccountVote[].class);
                }
                listener.onFinish(response, votes);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), AccountVote[].class);
            }
        }
        return null;
    }

    public static Discussion getContent(String permlink, final OnGetContentListener listener) {
        ArrayList<Object> params = new ArrayList<>();
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
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getObject(), Discussion.class);
            }
        }
        return null;
    }

    public static Discussion[] getContentReplies(String permlink, final OnGetContentRepliesListener listener) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(permlink);
        Response response = Connection.execute("database_api", "get_content_replies", params, listener == null ? null : new Connection.OnResponseListener() {
            @Override
            public void onFinish(Response response) {
                Discussion[] replies = null;
                if (response.isSuccess()) {
                    replies = Serializer.fromJson(response, response.getArray(), Discussion[].class);
                }
                listener.onFinish(response, replies);
            }
        });
        if ((response != null) && (listener == null)) {
            if (response.isSuccess()) {
                return Serializer.fromJson(response, response.getArray(), Discussion[].class);
            }
        }
        return null;
    }

    // Network broadcast API

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
                                   String jsonMetadata, boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("parent_author", parentAuthor);
        params.put("parent_permlink", parentPermlink);
        params.put("author", author);
        params.put("permlink", permlink);
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

    public static Response claimRewardBalance(PrivateKey privateKey, String account, Asset rewardAmalgam, Asset rewardABD, Asset rewardVests,
                                              boolean prepare, Connection.OnResponseListener listener) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", account);
        params.put("reward_amalgam", rewardAmalgam);
        params.put("reward_abd", rewardABD);
        params.put("reward_vests", rewardVests);
        return Connection.broadcast(privateKey, "claim_reward_balance", params, prepare, listener);
    }

    // Helper functions

    public static ExtendedAccount getAccount(String name, final OnGetExtendedAccountListener listener) {
        ExtendedAccount[] accounts = getAccounts(new String[]{name}, listener == null ? null : new OnGetExtendedAccountsListener() {
            @Override
            public void onFinish(Response response, ExtendedAccount[] accounts) {
                ExtendedAccount account = null;
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
