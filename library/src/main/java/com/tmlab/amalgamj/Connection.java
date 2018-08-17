package com.tmlab.amalgamj;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Connection {

    public interface OnResponseListener {
        void onFinish(Response response);
    }

    private static String mNodeUrl;
    private static int mId;

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 10;

    private static Executor mExecutor = Executors.newFixedThreadPool(4);

    private static final String TAG = Connection.class.getSimpleName();

    public static void setNodeUrl(String nodeUrl) {
        mNodeUrl = nodeUrl;
    }

    public static Response execute(final String apiName, final String command, final ArrayList<Object> params,
                                   final OnResponseListener listener) {
        if (listener == null) {
            return executeInternal(apiName, command, params);
        } else {
            (new AsyncTask<Void, Void, Response>() {
                @Override
                protected Response doInBackground(Void... voids) {
                    return executeInternal(apiName, command, params);
                }

                @Override
                protected void onPostExecute(Response result) {
                    if (listener != null) {
                        listener.onFinish(result);
                    }
                }
            }).executeOnExecutor(mExecutor);
            return null;
        }
    }

    public static Response broadcast(final PrivateKey privateKey, final String command, final LinkedHashMap<String, Object> params,
                                     final boolean prepare, final OnResponseListener listener) {
        if (listener == null) {
            return broadcastInternal(privateKey, command, params, prepare);
        } else {
            (new AsyncTask<Void, Void, Response>() {
                @Override
                protected Response doInBackground(Void... voids) {
                    return broadcastInternal(privateKey, command, params, prepare);
                }

                @Override
                protected void onPostExecute(Response result) {
                    if (listener != null) {
                        listener.onFinish(result);
                    }
                }
            }).executeOnExecutor(mExecutor);
            return null;
        }
    }

    public static String getJsonCommand(String apiName, String command, ArrayList<Object> params) throws Exception {
        JSONObject object = new JSONObject();
        object.put("jsonrpc", "2.0");
        object.put("method", "call");
        JSONArray callParams = new JSONArray();
        callParams.put(apiName);
        callParams.put(command);
        if (params == null) {
            params = new ArrayList<>();
        }
        callParams.put(new JSONArray(Serializer.toJson(params)));
        object.put("params", callParams);
        object.put("id", mId);
        return object.toString();
    }

    private static Response broadcastInternal(PrivateKey privateKey, String command, LinkedHashMap<String, Object> params, boolean prepare) {
        Response response = new Response();
        try {
            Transaction transaction = Transaction.prepare();
            transaction.addOperation(new Operation(command, params));
            transaction.sign(privateKey);
            ArrayList<Object> execParams = new ArrayList<>();
            execParams.add(transaction);
            if (prepare) {
                response.setRequest(getJsonCommand("network_broadcast_api", "broadcast_transaction_synchronous", execParams));
                response.setSuccess();
            } else {
                response = executeInternal("network_broadcast_api", "broadcast_transaction_synchronous", execParams);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            response.setError(e);
        }
        return response;
    }

    private static Response executeInternal(String apiName, String command, ArrayList<Object> params) {
        Response result = new Response();
        try {
            mId++;
            String request = getJsonCommand(apiName, command, params);
            result.setRequest(request);
            URL url = new URL(mNodeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setConnectTimeout(CONNECT_TIMEOUT * 1000);
                connection.setReadTimeout(READ_TIMEOUT * 1000);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                try {
                    outputStream.write(request.getBytes("UTF-8"));
                    outputStream.flush();
                } finally {
                    outputStream.close();
                }
                int responseCode;
                try {
                    responseCode = connection.getResponseCode();
                } catch (IOException e) {
                    responseCode = connection.getResponseCode();
                }
                InputStream inputStream;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                } else {
                    inputStream = connection.getErrorStream();
                }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        result = new Response(sb.toString());
                    } finally {
                        br.close();
                    }
                } finally {
                    inputStream.close();
                }
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            result.setError(e);
        }
        return result;
    }
}
