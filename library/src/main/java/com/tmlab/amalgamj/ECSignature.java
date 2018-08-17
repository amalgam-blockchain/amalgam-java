package com.tmlab.amalgamj;

import org.joou.UByte;

import java.math.BigInteger;

public class ECSignature {

    public BigInteger r;
    public BigInteger s;

    public ECSignature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    public byte[] toDER() {
        byte[] rBa = r.toByteArray();
        byte[] sBa = s.toByteArray();
        ByteBuffer buffer = new ByteBuffer();
        buffer.writeUint8(UByte.valueOf(0x30));
        buffer.writeUint8(UByte.valueOf(rBa.length + sBa.length + 4));
        buffer.writeUint8(UByte.valueOf(0x02));
        buffer.writeUint8(UByte.valueOf(rBa.length));
        buffer.write(rBa);
        buffer.writeUint8(UByte.valueOf(0x02));
        buffer.writeUint8(UByte.valueOf(sBa.length));
        buffer.write(sBa);
        return buffer.toByteArray();
    }
}
