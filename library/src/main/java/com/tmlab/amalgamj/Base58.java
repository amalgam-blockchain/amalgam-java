package com.tmlab.amalgamj;

import java.math.BigInteger;

public class Base58 {

    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(58);

    public static String encode(byte[] input) {
        BigInteger value = new BigInteger(1, input);
        StringBuilder s = new StringBuilder();
        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] qr = value.divideAndRemainder(BASE);
            value = qr[0];
            s.insert(0, ALPHABET.charAt(qr[1].intValue()));
        }
        // Deal with leading zeros
        for (byte b : input) {
            if (b == 0) {
                s.insert(0, ALPHABET.charAt(0));
            } else {
                break;
            }
        }
        return s.toString();
    }

    public static byte[] decode(String input) {
        BigInteger res = BigInteger.valueOf(0);
        int length = input.length();
        for (int i = 0; i < length; i++) {
            res = res.multiply(BASE).add(BigInteger.valueOf(ALPHABET.indexOf(input.charAt(i))));
        }
        byte[] bytes = res.toByteArray();
        // Deal with leading zeros
        boolean stripSignByte = bytes.length > 1 && bytes[0] == 0 && bytes[1] < 0;
        int leadingZeros = 0;
        int i = 0;
        while ((i < length) && (input.charAt(i) == ALPHABET.charAt(0))) {
            leadingZeros++;
            i++;
        }
        length = bytes.length - (stripSignByte ? 1 : 0);
        byte[] tmp = new byte[length + leadingZeros];
        if (length > 0) {
            System.arraycopy(bytes, stripSignByte ? 1 : 0, tmp, leadingZeros, length);
        }
        return tmp;
    }
}
