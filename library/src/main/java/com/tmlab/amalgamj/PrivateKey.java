package com.tmlab.amalgamj;

import org.joou.UByte;

import java.math.BigInteger;
import java.util.Arrays;

public class PrivateKey {

    public BigInteger d;
    private PublicKey mPublicKey;

    public PrivateKey(BigInteger d) {
        this.d = d;
    }

    public static PrivateKey fromWif(String privateWif) throws Exception {
        byte[] bytes = Base58.decode(privateWif);
        byte version = bytes[0];
        if (version != (byte) 0x80) {
            throw new Exception("Expected version 128, instead got " + Integer.toString(version));
        }
        byte[] private_key = Arrays.copyOf(bytes, bytes.length - 4);
        byte[] checksum = Arrays.copyOfRange(bytes, bytes.length - 4, bytes.length);
        byte[] new_checksum = Utils.hashSha256(private_key);
        new_checksum = Utils.hashSha256(new_checksum);
        new_checksum = Arrays.copyOf(new_checksum, 4);
        if (!Arrays.equals(new_checksum, checksum)) {
            throw new Exception("Invalid WIF key (checksum did not match)");
        }
        private_key = Arrays.copyOfRange(private_key, 1, private_key.length);
        int length = private_key.length;
        if (length != 32) {
            throw new Exception("Expecting 32 bytes for private_key, instead got " + Integer.toString(length));
        }
        return new PrivateKey(Utils.bigIntFromBuffer(private_key));
    }

    public static boolean isWif(String privateWif) {
        try {
            fromWif(privateWif);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String toWif() {
        ByteBuffer buffer = new ByteBuffer();
        buffer.writeUint8(UByte.valueOf(0x80));
        buffer.write(Utils.bigIntToBuffer(d, 32));
        byte[] checksum = Utils.hashSha256(buffer.toByteArray());
        checksum = Utils.hashSha256(checksum);
        buffer.write(checksum, 0, 4);
        return Base58.encode(buffer.toByteArray());
    }

    public String toString() {
        return toWif();
    }

    public PublicKey toPublicKey() {
        if (mPublicKey != null) {
            return mPublicKey;
        }
        mPublicKey = new PublicKey(ECDSA.G.multiply(d));
        return mPublicKey;
    }
}
