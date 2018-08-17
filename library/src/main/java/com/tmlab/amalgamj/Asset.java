package com.tmlab.amalgamj;

import android.util.Log;

import java.math.BigInteger;

public class Asset {

    public long amount;
    public AssetSymbol symbol;

    private static final String TAG = Asset.class.getSimpleName();

    public Asset(long amount, AssetSymbol symbol) {
        this.amount = amount;
        this.symbol = symbol;
    }

    public static Asset fromString(String s) throws Exception {
        s = s.trim();
        if (!s.matches("^[0-9]+\\.?[0-9]* [A-Za-z0-9]+$")) {
            throw new Exception("Expecting amount like \"99.000 SYMBOL\", instead got \"" + s + "\"");
        }
        String[] parts = s.split(" ");
        String amount = parts[0];
        String symbol = parts[1];
        if (symbol.length() > 6) {
            throw new Exception("Symbols are not longer than 6 characters, " + symbol + "-" + Integer.toString(symbol.length()));
        }
        AssetSymbol assetSymbol = AssetSymbol.fromString(symbol);
        int dot = amount.indexOf(".");
        int precision = dot == -1 ? 0 : amount.length() - dot - 1;
        if (precision != assetSymbol.precision) {
            throw new Exception("Wrong precision, expected " + Integer.toString(assetSymbol.precision) + ", instead got " + Integer.toString(precision));
        }
        return new Asset(Long.parseLong(amount.replace(".", "")), assetSymbol);
    }

    public String toString() {
        String amountStr = Long.toString(amount);
        if (symbol.precision > 0) {
            while (amountStr.length() <= symbol.precision) {
                amountStr = "0" + amountStr;
            }
            amountStr = amountStr.substring(0, amountStr.length() - symbol.precision) + "." + amountStr.substring(amountStr.length() - symbol.precision);
        }
        return amountStr + " " + symbol.name;
    }

    public double getDouble() {
        return amount / Math.pow(10, symbol.precision);
    }

    public Asset multiply(Price price) {
        try {
            return multiplyOrThrow(price);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            return new Asset(0, symbol);
        }
    }

    public Asset multiplyOrThrow(Price price) throws Exception {
        if (symbol.equals(price.base.symbol)) {
            if (price.base.amount <= 0) {
                throw new Exception("Price base must be > 0");
            }
            BigInteger result = BigInteger.valueOf(amount).multiply(BigInteger.valueOf(price.quote.amount)).divide(BigInteger.valueOf(price.base.amount));
            return new Asset(result.longValue(), price.quote.symbol);
        } else if (symbol.equals(price.quote.symbol)) {
            if (price.quote.amount <= 0) {
                throw new Exception("Price quote must be > 0");
            }
            BigInteger result = BigInteger.valueOf(amount).multiply(BigInteger.valueOf(price.base.amount)).divide(BigInteger.valueOf(price.quote.amount));
            return new Asset(result.longValue(), price.base.symbol);
        }
        throw new Exception("Provided asset does not fulfil the requirements to perform the multiply operation");
    }

    public Asset add(Asset asset) throws Exception {
        if (!symbol.equals(asset.symbol)) {
            throw new Exception("Asset symbols are not identical");
        }
        return new Asset(amount + asset.amount, symbol);
    }

    public Asset subtract(Asset asset) throws Exception {
        if (!symbol.equals(asset.symbol)) {
            throw new Exception("Asset symbols are not identical");
        }
        return new Asset(amount - asset.amount, symbol);
    }
}
