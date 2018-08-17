package com.tmlab.amalgamj;

import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static byte[] hashSha256(byte[] value) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(value);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            return null;
        }
    }

    public static byte[] hashHmacSha256(byte[] value, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(value);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            return null;
        }
    }

    public static byte[] bigIntToBuffer(BigInteger value, int size) {
        byte[] bytes = value.toByteArray();
        boolean stripSignByte = bytes[0] == 0;
        int length = bytes.length - (stripSignByte ? 1 : 0);
        int padding = size - length;
        if (padding < 0) {
            padding = 0;
        }
        byte[] tmp = new byte[length + padding];
        if (length > 0) {
            System.arraycopy(bytes, stripSignByte ? 1 : 0, tmp, padding, length);
        }
        return tmp;
    }

    public static BigInteger bigIntFromBuffer(byte[] bytes) {
        if ((bytes[0] & 0x80) != 0) {
            int length = bytes.length;
            byte[] tmp = new byte[length + 1];
            System.arraycopy(bytes, 0, tmp, 1, length);
            return new BigInteger(tmp);
        }
        return new BigInteger(bytes);
    }

    public static byte[] singleByte(byte b) {
        byte[] bytes = new byte[1];
        bytes[0] = b;
        return bytes;
    }

    public static byte[] concat(byte[]... arrays) {
        ByteBuffer buffer = new ByteBuffer();
        for (byte[] array : arrays) {
            buffer.write(array);
        }
        return buffer.toByteArray();
    }

    public static byte[] hex2bin(String value) {
        byte[] bytes = new byte[value.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            bytes[i] = (byte) Integer.parseInt(value.substring(index, index + 2), 16);
        }
        return bytes;
    }

    public static String bin2hex(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (byte b : bytes) {
            s.append(String.format("%02x", b));
        }
        return s.toString();
    }
}
