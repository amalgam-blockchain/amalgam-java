package com.tmlab.amalgamj;

import org.joou.UByte;

import java.math.BigInteger;
import java.util.Arrays;

public class Point {

    public BigInteger x;
    public BigInteger y;
    public BigInteger z;
    private BigInteger mZInv;
    public boolean compressed;
    private static BigInteger THREE = BigInteger.valueOf(3);

    public Point(BigInteger x, BigInteger y, BigInteger z) {
        this.x = x;
        this.y = y;
        this.z = z;
        mZInv = null;
        compressed = true;
    }

    public BigInteger getZInv() {
        if (mZInv == null) {
            mZInv = z.modInverse(ECDSA.p);
        }
        return mZInv;
    }

    public BigInteger getAffineX() {
        return x.multiply(getZInv()).mod(ECDSA.p);
    }

    public BigInteger getAffineY() {
        return y.multiply(getZInv()).mod(ECDSA.p);
    }

    public static Point fromAffine(BigInteger x, BigInteger y) {
        return new Point(x, y, BigInteger.ONE);
    }

    public boolean equals(Point other) {
        if (other == this) {
            return true;
        }
        if (ECDSA.isInfinity(this)) {
            return ECDSA.isInfinity(other);
        }
        if (ECDSA.isInfinity(other)) {
            return ECDSA.isInfinity(this);
        }
        // u = Y2 * Z1 - Y1 * Z2
        BigInteger u = other.y.multiply(z).subtract(y.multiply(other.z)).mod(ECDSA.p);
        if (u.signum() != 0) {
            return false;
        }
        // v = X2 * Z1 - X1 * Z2
        BigInteger v = other.x.multiply(z).subtract(x.multiply(other.z)).mod(ECDSA.p);
        return v.signum() == 0;
    }

    public Point negate() {
        return new Point(x, ECDSA.p.subtract(y), z);
    }

    public Point add(Point b) {
        if (ECDSA.isInfinity(this)) {
            return b;
        }
        if (ECDSA.isInfinity(b)) {
            return this;
        }
        BigInteger x1 = x;
        BigInteger y1 = y;
        BigInteger x2 = b.x;
        BigInteger y2 = b.y;
        // u = Y2 * Z1 - Y1 * Z2
        BigInteger u = y2.multiply(z).subtract(y1.multiply(b.z)).mod(ECDSA.p);
        // v = X2 * Z1 - X1 * Z2
        BigInteger v = x2.multiply(z).subtract(x1.multiply(b.z)).mod(ECDSA.p);
        if (v.signum() == 0) {
            if (u.signum() == 0) {
                return twice(); // this == b, so double
            }
            return ECDSA.INFINITY; // this = -b, so infinity
        }
        BigInteger v2 = v.pow(2);
        BigInteger v3 = v2.multiply(v);
        BigInteger x1v2 = x1.multiply(v2);
        BigInteger zu2 = u.pow(2).multiply(z);
        // x3 = v * (z2 * (z1 * u^2 - 2 * x1 * v^2) - v^3)
        BigInteger x3 = zu2.subtract(x1v2.shiftLeft(1)).multiply(b.z).subtract(v3).multiply(v).mod(ECDSA.p);
        // y3 = z2 * (3 * x1 * u * v^2 - y1 * v^3 - z1 * u^3) + u * v^3
        BigInteger y3 = x1v2.multiply(THREE).multiply(u).subtract(y1.multiply(v3)).subtract(zu2.multiply(u)).multiply(b.z).add(u.multiply(v3)).mod(ECDSA.p);
        // z3 = v^3 * z1 * z2
        BigInteger z3 = v3.multiply(z).multiply(b.z).mod(ECDSA.p);
        return new Point(x3, y3, z3);
    }

    public Point twice() {
        if (ECDSA.isInfinity(this)) {
            return this;
        }
        if (y.signum() == 0) {
            return ECDSA.INFINITY;
        }
        BigInteger x1 = x;
        BigInteger y1 = y;
        BigInteger y1z1 = y1.multiply(z).mod(ECDSA.p);
        BigInteger y1sqz1 = y1z1.multiply(y1).mod(ECDSA.p);
        BigInteger a = ECDSA.a;
        // w = 3 * x1^2 + a * z1^2
        BigInteger w = x1.pow(2).multiply(THREE);
        if (a.signum() != 0) {
            w = w.add(z.pow(2).multiply(a));
        }
        w = w.mod(ECDSA.p);
        // x3 = 2 * y1 * z1 * (w^2 - 8 * x1 * y1^2 * z1)
        BigInteger x3 = w.pow(2).subtract(x1.shiftLeft(3).multiply(y1sqz1)).shiftLeft(1).multiply(y1z1).mod(ECDSA.p);
        // y3 = 4 * y1^2 * z1 * (3 * w * x1 - 2 * y1^2 * z1) - w^3
        BigInteger y3 = w.multiply(THREE).multiply(x1).subtract(y1sqz1.shiftLeft(1)).shiftLeft(2).multiply(y1sqz1).subtract(w.pow(3)).mod(ECDSA.p);
        // z3 = 8 * (y1 * z1)^3
        BigInteger z3 = y1z1.pow(3).shiftLeft(3).mod(ECDSA.p);
        return new Point(x3, y3, z3);
    }

    // Simple NAF (Non-Adjacent Form) multiplication algorithm
    public Point multiply(BigInteger k) {
        if (ECDSA.isInfinity(this)) {
            return this;
        }
        if (k.signum() == 0) {
            return ECDSA.INFINITY;
        }
        BigInteger e = k;
        BigInteger h = e.multiply(THREE);
        Point neg = negate();
        Point R = this;
        for (int i = h.bitLength() - 2; i > 0; --i) {
            boolean hBit = h.testBit(i);
            boolean eBit = e.testBit(i);
            R = R.twice();
            if (hBit != eBit) {
                R = R.add(hBit ? this : neg);
            }
        }
        return R;
    }

    // Compute this*j + xx*k (simultaneous multiplication)
    public Point multiplyTwo(BigInteger j, Point xx, BigInteger k) {
        int i = Math.max(j.bitLength(), k.bitLength()) - 1;
        Point R = ECDSA.INFINITY;
        Point both = add(xx);
        while (i >= 0) {
            boolean jBit = j.testBit(i);
            boolean kBit = k.testBit(i);
            R = R.twice();
            if (jBit) {
                if (kBit) {
                    R = R.add(both);
                } else {
                    R = R.add(this);
                }
            } else if (kBit) {
                R = R.add(xx);
            }
            --i;
        }
        return R;
    }

    public byte[] getEncoded() {
        return getEncoded(compressed);
    }

    public byte[] getEncoded(boolean isCompressed) {
        ByteBuffer buffer = new ByteBuffer();
        if (ECDSA.isInfinity(this)) {
            buffer.writeUint8(UByte.valueOf(0));
            return buffer.toByteArray();
        }
        BigInteger x1 = getAffineX();
        BigInteger y1 = getAffineY();
        if (isCompressed) {
            buffer.writeUint8(UByte.valueOf(y1.testBit(0) ? 3 : 2));
            buffer.write(Utils.bigIntToBuffer(x1, ECDSA.pLength));
        } else {
            buffer.writeUint8(UByte.valueOf(4));
            buffer.write(Utils.bigIntToBuffer(x1, ECDSA.pLength));
            buffer.write(Utils.bigIntToBuffer(y1, ECDSA.pLength));
        }
        return buffer.toByteArray();
    }

    public static Point decodeFrom(byte[] bytes) throws Exception {
        byte type = bytes[0];
        boolean compressed = (type != 4);
        BigInteger x = Utils.bigIntFromBuffer(Arrays.copyOfRange(bytes, 1, ECDSA.pLength + 1));
        Point Q;
        if (compressed) {
            if (bytes.length != ECDSA.pLength + 1) {
                throw new Exception("Invalid sequence length");
            }
            if ((type != 2) && (type != 3)) {
                throw new Exception("Invalid sequence tag");
            }
            boolean isOdd = (type == 3);
            Q = ECDSA.pointFromX(isOdd, x);
        } else {
            if (bytes.length != ECDSA.pLength + ECDSA.pLength + 1) {
                throw new Exception("Invalid sequence length");
            }
            BigInteger y = Utils.bigIntFromBuffer(Arrays.copyOfRange(bytes, ECDSA.pLength + 1, bytes.length));
            Q = fromAffine(x, y);
        }
        Q.compressed = compressed;
        return Q;
    }
}
