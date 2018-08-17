package com.tmlab.amalgamj;

import java.util.Arrays;

public class PublicKey {

    public static String ADDRESS_PREFIX = "AML";

    public Point Q;

    public PublicKey(Point Q) {
        this.Q = Q;
    }

    public static PublicKey fromString(String publicKey) throws Exception {
        String prefix = publicKey.substring(0, ADDRESS_PREFIX.length());
        if (!ADDRESS_PREFIX.equals(prefix)) {
            throw new Exception("Expecting key to begin with " + ADDRESS_PREFIX + ", instead got " + prefix);
        }
        byte[] bytes = Base58.decode(publicKey.substring(ADDRESS_PREFIX.length()));
        byte[] public_key = Arrays.copyOf(bytes, bytes.length - 4);
        byte[] checksum = Arrays.copyOfRange(bytes, bytes.length - 4, bytes.length);
        byte[] new_checksum = Ripemd160.hash(public_key);
        new_checksum = Arrays.copyOf(new_checksum, 4);
        if (!Arrays.equals(new_checksum, checksum)) {
            throw new Exception("Invalid public key (checksum did not match)");
        }
        return new PublicKey(Point.decodeFrom(public_key));
    }

    public String toString() {
        byte[] pubBuf = getEncoded();
        byte[] checksum = Ripemd160.hash(pubBuf);
        ByteBuffer buffer = new ByteBuffer();
        buffer.write(pubBuf);
        buffer.write(checksum, 0, 4);
        return ADDRESS_PREFIX + Base58.encode(buffer.toByteArray());
    }

    public byte[] getEncoded() {
        return getEncoded(Q.compressed);
    }

    public byte[] getEncoded(boolean isCompressed) {
        return Q.getEncoded(isCompressed);
    }
}
