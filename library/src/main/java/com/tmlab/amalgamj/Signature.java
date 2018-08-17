package com.tmlab.amalgamj;

import org.joou.UByte;

import java.math.BigInteger;
import java.util.Arrays;

public class Signature {

    public BigInteger r;
    public BigInteger s;
    public byte i;

    public Signature(BigInteger r, BigInteger s, byte i) {
        this.r = r;
        this.s = s;
        this.i = i;
    }

    public static Signature fromString(String value) throws Exception {
        byte[] bytes = Utils.hex2bin(value);
        if (bytes.length != 65) {
            throw new Exception("Invalid signature length");
        }
        byte i = bytes[0];
        if ((i - 27) != ((i - 27) & 7)){
            throw new Exception("Invalid signature parameter");
        }
        BigInteger r = Utils.bigIntFromBuffer(Arrays.copyOfRange(bytes, 1, 33));
        BigInteger s = Utils.bigIntFromBuffer(Arrays.copyOfRange(bytes, 33, 65));
        return new Signature(r, s, i);
    }

    public String toString() {
        ByteBuffer buffer = new ByteBuffer();
        buffer.writeUint8(UByte.valueOf(i));
        buffer.write(Utils.bigIntToBuffer(r, 32));
        buffer.write(Utils.bigIntToBuffer(s, 32));
        return Utils.bin2hex(buffer.toByteArray());
    }

    public static Signature sign(String msg, PrivateKey privateKey) throws Exception {
        return sign(msg.getBytes("UTF-8"), privateKey);
    }

    public static Signature sign(byte[] msg, PrivateKey privateKey) throws Exception {
        byte[] hash = Utils.hashSha256(msg);
        BigInteger e = Utils.bigIntFromBuffer(hash);
        PublicKey publicKey = privateKey.toPublicKey();
        byte i;
        int nonce = 1;
        ECSignature ecSignature;
        while (true) {
            ecSignature = ECDSA.createSignature(hash, privateKey.d, nonce++);
            byte[] der = ecSignature.toDER();
            byte lenR = der[3];
            byte lenS = der[5 + lenR];
            if ((lenR == 32) && (lenS == 32)) {
                i = ECDSA.calcPubKeyRecoveryParam(e, ecSignature, publicKey.Q);
                i += 4; // compressed
                i += 27; // compact // 24 or 27 : (forcing odd-y 2nd key candidate)
                break;
            }
        }
        return new Signature(ecSignature.r, ecSignature.s, i);
    }

    public static boolean verify(String msg, String signature, PublicKey publicKey) {
        byte[] bytes = null;
        try {
            bytes = msg.getBytes("UTF-8");
        } catch (Exception e) {
        }
        return verify(bytes, signature, publicKey);
    }

    public static boolean verify(byte[] msg, String signature, PublicKey publicKey) {
        Signature s;
        BigInteger e;
        try {
            s = fromString(signature);
            byte[] hash = Utils.hashSha256(msg);
            e = Utils.bigIntFromBuffer(hash);
        } catch (Exception ex) {
            return false;
        }
        return ECDSA.verify(e, new ECSignature(s.r, s.s), publicKey.Q);
    }
}
