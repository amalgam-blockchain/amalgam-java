package com.tmlab.amalgamj;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Response extends JSONObject {

    private String mRequest;
    private String mError;
    private boolean mNotFound;
    private boolean mNoConnection;

    public Response() {
        super();
    }

    public Response(String json) throws JSONException {
        super(json);
    }

    @Override
    public int getInt(String name) {
        int result = 0;
        try {
            if (has(name)) {
                result = super.getInt(name);
            }
        } catch (JSONException e) {
        }
        return result;
    }

    @Override
    public String getString(String name) {
        String result = "";
        try {
            if (has(name) && !isNull(name)) {
                result = super.getString(name);
            }
        } catch (JSONException e) {
        }
        return result;
    }

    @Override
    public boolean getBoolean(String name) {
        boolean result = false;
        try {
            if (has(name)) {
                try {
                    result = super.getBoolean(name);
                } catch (JSONException e) {
                    result = super.getInt(name) == 1;
                }
            }
        } catch (JSONException e) {
        }
        return result;
    }

    @Override
    public JSONArray getJSONArray(String name) {
        JSONArray result = new JSONArray();
        try {
            if (has(name)) {
                result = super.getJSONArray(name);
            }
        } catch (JSONException e) {
        }
        return result;
    }

    @Override
    public JSONObject getJSONObject(String name) {
        JSONObject result = new JSONObject();
        try {
            if (has(name)) {
                result = super.getJSONObject(name);
            }
        } catch (JSONException e) {
        }
        return result;
    }

    public String getRequest() {
        return mRequest;
    }

    public void setRequest(String request) {
        mRequest = request;
    }

    public boolean isSuccess() {
        return has("result") && TextUtils.isEmpty(mError);
    }

    public String getArray() {
        return getJSONArray("result").toString();
    }

    public String getObject() {
        return getJSONObject("result").toString();
    }

    public String getObjectField(String name) {
        JSONObject result = new JSONObject();
        JSONObject object = getJSONObject("result");
        try {
            if (has(name)) {
                result = object.getJSONObject(name);
            }
        } catch (JSONException e) {
        }
        return result.toString();
    }

    public void setSuccess() {
        try {
            put("result", new JSONObject());
        } catch (JSONException e) {
        }
    }

    public void setError(Exception e) {
        if ((e instanceof java.net.SocketException) ||
                (e instanceof java.net.SocketTimeoutException) ||
                (e instanceof java.net.UnknownHostException) ||
                (e instanceof javax.net.ssl.SSLException)) {
            mError = "No internet connection. Check your network and try again";
            mNoConnection = true;
        } else {
            String error = e.getMessage();
            mError = TextUtils.isEmpty(error) ? e.toString() : error;
            mNoConnection = false;
        }
    }

    public String getError() {
        if (TextUtils.isEmpty(mError)) {
            if (has("error")) {
                JSONObject error = getJSONObject("error");
                if (error.has("message")) {
                    String message = error.optString("message");
                    int begin = message.indexOf(":");
                    begin = (begin == -1) ? 0 : begin + 1;
                    int end = message.indexOf("{");
                    if (end == -1) {
                        end = message.length();
                    }
                    message = message.substring(begin, end).trim();
                    message = message.substring(0, 1).toUpperCase() + message.substring(1);
                    return message;
                }
            }
            return "";
        } else {
            return mError;
        }
    }

    public boolean isNotFound() {
        return mNotFound;
    }

    public void setNotFound() {
        mNotFound = true;
        mError = "Object not found";
    }

    public boolean isNoConnection() {
        return mNoConnection;
    }

    public void checkError() throws Exception {
        if (isSuccess()) {
            return;
        }
        if (isNoConnection()) {
            throw new java.net.SocketException(getError());
        } else {
            throw new Exception(getError());
        }
    }
}
