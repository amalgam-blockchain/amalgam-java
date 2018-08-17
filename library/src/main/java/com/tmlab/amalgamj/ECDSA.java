package com.tmlab.amalgamj;

import java.math.BigInteger;
import java.util.Arrays;

public class ECDSA {

    public static BigInteger a = BigInteger.ZERO;
    public static BigInteger b = BigInteger.valueOf(7);
    public static BigInteger p = new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f", 16);
    public static BigInteger n = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
    public static Point G = Point.fromAffine(
            new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16),
            new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8", 16)
    );
    public static Point INFINITY = new Point(null, null, BigInteger.ZERO);
    // result caching
    public static BigInteger pOverFour = p.add(BigInteger.ONE).shiftRight(2);
    // determine size of p in bytes
    public static int pLength = (int) Math.floor((p.bitLength() + 7) / 8);

    public static Point pointFromX(boolean isOdd, BigInteger x) {
        BigInteger alpha = x.pow(3).add(a.multiply(x)).add(b).mod(p);
        BigInteger beta = alpha.modPow(pOverFour, p); // XXX: not compatible with all curves
        BigInteger y = beta;
        if (!beta.testBit(0) ^ !isOdd) {
            y = p.subtract(y); // -y % p
        }
        return Point.fromAffine(x, y);
    }

    public static boolean isInfinity(Point Q) {
        if (Q == INFINITY) {
            return true;
        }
        return (Q.z.signum() == 0) && (Q.y.signum() != 0);
    }

    public static boolean isOnCurve(Point Q) {
        if (isInfinity(Q)) {
            return true;
        }
        BigInteger x = Q.getAffineX();
        BigInteger y = Q.getAffineY();
        // Check that xQ and yQ are integers in the interval [0, p - 1]
        if ((x.signum() < 0) || (x.compareTo(p) >= 0)) {
            return false;
        }
        if ((y.signum() < 0) || (y.compareTo(p) >= 0)) {
            return false;
        }
        // and check that y^2 = x^3 + ax + b (mod p)
        BigInteger lhs = y.pow(2).mod(p);
        BigInteger rhs = x.pow(3).add(a.multiply(x)).add(b).mod(p);
        return lhs.equals(rhs);
    }

    /**
     * Validate an elliptic curve point.
     *
     * See SEC 1, section 3.2.2.1: Elliptic Curve Public Key Validation Primitive
     */
    public static boolean validate(Point Q) throws Exception {
        // Check Q != O
        if (isInfinity(Q)) {
            throw new Exception("Point is at infinity");
        }
        if (!isOnCurve(Q)) {
            throw new Exception("Point is not on the curve");
        }
        // Check nQ = O (where Q is a scalar multiple of G)
        Point nQ = Q.multiply(n);
        if (!isInfinity(nQ)) {
            throw new Exception("Point is not a scalar multiple of G");
        }
        return true;
    }

    /**
     * Deterministic generation of k value.
     *
     * https://tools.ietf.org/html/rfc6979#section-3.2
     */
    private static BigInteger deterministicGenerateK(byte[] hash, BigInteger d, int nonce, ECSignature signature) throws Exception {
        BigInteger e = Utils.bigIntFromBuffer(hash);
        if (nonce != 0) {
            hash = Utils.hashSha256(Utils.concat(hash, Utils.singleByte((byte) nonce)));
        }
        // sanity check
        if (hash.length != 32) {
            throw new Exception("Hash must be 256 bit");
        }
        byte[] x = Utils.bigIntToBuffer(d, 32);
        // Step B
        byte[] v = new byte[32];
        Arrays.fill(v, (byte) 1);
        // Step C
        byte[] k = new byte[32];
        // Step D
        k = Utils.hashHmacSha256(Utils.concat(v, Utils.singleByte((byte) 0), x, hash), k);
        // Step E
        v = Utils.hashHmacSha256(v, k);
        // Step F
        k = Utils.hashHmacSha256(Utils.concat(v, Utils.singleByte((byte) 1), x, hash), k);
        // Step G
        v = Utils.hashHmacSha256(v, k);
        // Step H1/H2a, ignored as tlen === qlen (256 bit)
        // Step H2b
        v = Utils.hashHmacSha256(v, k);
        BigInteger T = Utils.bigIntFromBuffer(v);
        // Step H3, repeat until T is within the interval [1, n - 1]
        while ((T.signum() <= 0) || (T.compareTo(n) >= 0) || !checkSignature(T, e, d, signature)) {
            k = Utils.hashHmacSha256(Utils.concat(v, Utils.singleByte((byte) 0)), k);
            v = Utils.hashHmacSha256(v, k);
            // Step H1/H2a, again, ignored as tlen === qlen (256 bit)
            // Step H2b again
            v = Utils.hashHmacSha256(v, k);
            T = Utils.bigIntFromBuffer(v);
        }
        return T;
    }

    private static boolean checkSignature(BigInteger k, BigInteger e, BigInteger d, ECSignature signature) {
        // find canonically valid signature
        Point Q = G.multiply(k);
        if (isInfinity(Q)) {
            return false;
        }
        signature.r = Q.getAffineX().mod(n);
        if (signature.r.signum() == 0) {
            return false;
        }
        signature.s = k.modInverse(n).multiply(e.add(d.multiply(signature.r))).mod(n);
        if (signature.s.signum() == 0) {
            return false;
        }
        return true;
    }

    public static ECSignature createSignature(byte[] hash, BigInteger d, int nonce) throws Exception {
        BigInteger e = Utils.bigIntFromBuffer(hash);
        ECSignature signature = new ECSignature(null, null);
        deterministicGenerateK(hash, d, nonce, signature);
        BigInteger nOverTwo = n.shiftRight(1);
        // enforce low S values, see bip62: 'low s values in signatures'
        if (signature.s.compareTo(nOverTwo) > 0) {
            signature.s = n.subtract(signature.s);
        }
        return signature;
    }

    public static boolean verify(BigInteger e, ECSignature signature, Point Q) {
        BigInteger r = signature.r;
        BigInteger s = signature.s;
        // 1.4.1 Enforce r and s are both integers in the interval [1, n - 1]
        if ((r.signum() <= 0) || (r.compareTo(n) >= 0)) {
            return false;
        }
        if ((s.signum() <= 0) || (s.compareTo(n) >= 0)) {
            return false;
        }
        // c = s^-1 mod n
        BigInteger c = s.modInverse(n);
        // 1.4.4 Compute u1 = es^-1 mod n
        //               u2 = rs^-1 mod n
        BigInteger u1 = e.multiply(c).mod(n);
        BigInteger u2 = r.multiply(c).mod(n);
        // 1.4.5 Compute R = (xR, yR) = u1G + u2Q
        Point R = G.multiplyTwo(u1, Q, u2);
        // 1.4.5 (cont.) Enforce R is not at infinity
        if (isInfinity(R)) {
            return false;
        }
        // 1.4.6 Convert the field element R.x to an integer
        BigInteger xR = R.getAffineX();
        // 1.4.7 Set v = xR mod n
        BigInteger v = xR.mod(n);
        // 1.4.8 If v = r, output "valid", and if v != r, output "invalid"
        return v.equals(r);
    }

    /**
     * Recover a public key from a signature.
     *
     * See SEC 1: Elliptic Curve Cryptography, section 4.1.6, "Public Key Recovery Operation"
     *
     * http://www.secg.org/download/aid-780/sec1-v2.pdf
     */
    private static Point recoverPubKey(BigInteger e, ECSignature signature, byte i) throws Exception {
        if ((i & 3) != i) {
            throw new Exception("Recovery param is more than two bits");
        }
        BigInteger r = signature.r;
        BigInteger s = signature.s;
        if (!((r.signum() > 0) && (r.compareTo(n) < 0))) {
            throw new Exception("Invalid r value");
        }
        if (!((s.signum() > 0) && (s.compareTo(n) < 0))) {
            throw new Exception("Invalid s value");
        }
        // A set LSB signifies that the y-coordinate is odd
        boolean isYOdd = (i & 1) != 0;
        // The more significant bit specifies whether we should use the
        // first or second candidate key.
        boolean isSecondKey = (i >> 1) != 0;
        // 1.1 Let x = r + jn
        BigInteger x = isSecondKey ? r.add(n) : r;
        Point R = pointFromX(isYOdd, x);
        // 1.4 Check that nR is at infinity
        Point nR = R.multiply(n);
        if (!isInfinity(nR)) {
            throw new Exception("nR is not a valid curve point");
        }
        // Compute -e from e
        BigInteger eNeg = e.negate().mod(n);
        // 1.6.1 Compute Q = r^-1 (sR -  eG)
        //               Q = r^-1 (sR + -eG)
        BigInteger rInv = r.modInverse(n);
        Point Q = R.multiplyTwo(s, G, eNeg).multiply(rInv);
        validate(Q);
        return Q;
    }

    /**
     * Calculate pubkey extraction parameter.
     *
     * When extracting a pubkey from a signature, we have to
     * distinguish four different cases. Rather than putting this
     * burden on the verifier, Bitcoin includes a 2-bit value with the
     * signature.
     *
     * This function simply tries all four cases and returns the value
     * that resulted in a successful pubkey recovery.
     */
    public static byte calcPubKeyRecoveryParam(BigInteger e, ECSignature signature, Point Q) throws Exception {
        for (byte i = 0; i < 4; i++) {
            Point Qprime = recoverPubKey(e, signature, i);
            // 1.6.2 Verify Q
            if (Qprime.equals(Q)) {
                return i;
            }
        }
        throw new Exception("Unable to find valid recovery factor");
    }
}
