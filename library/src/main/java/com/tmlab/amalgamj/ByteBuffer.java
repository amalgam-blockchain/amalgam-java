package com.tmlab.amalgamj;

import org.joou.UByte;
import org.joou.UInteger;
import org.joou.ULong;
import org.joou.UShort;

import java.util.Arrays;

public class ByteBuffer {

    private byte[] mBuffer = new byte[32];
    private int mOffset;

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > mBuffer.length) {
            int newCapacity = mBuffer.length << 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            mBuffer = Arrays.copyOf(mBuffer, newCapacity);
        }
    }

    public synchronized int length() {
        return mOffset;
    }

    public synchronized byte toByteArray()[] {
        return Arrays.copyOf(mBuffer, mOffset);
    }

    public synchronized boolean hasData(int offset, int length) {
        return mOffset >= (offset + length);
    }

    public synchronized byte[] read(int offset, int length) {
        return Arrays.copyOfRange(mBuffer, offset, offset + length);
    }

    public synchronized void write(byte[] value) {
        write(value, 0, value.length);
    }

    public synchronized void write(byte[] value, int offset, int length) {
        ensureCapacity(mOffset + length);
        System.arraycopy(value, offset, mBuffer, mOffset, length);
        mOffset += length;
    }

    public synchronized long readInt(int offset, int length) {
        long result = 0;
        long mask = 0;
        for (int i = 0; i < length; i++) {
            int bits = i * 8;
            result |= (mBuffer[offset + i] & 0xFF) << bits;
            mask |= 0xFFL << bits;
        }
        return result & mask;
    }

    public synchronized void writeInt(long value, int length) {
        ensureCapacity(mOffset + length);
        for (int i = 0; i < length; i++) {
            int bits = i * 8;
            mBuffer[mOffset + i] = (byte) ((value & (0xFFL << bits)) >> bits);
        }
        mOffset += length;
    }

    public synchronized UByte readUint8(int offset) {
        return UByte.valueOf(mBuffer[offset] & 0xFF);
    }

    public synchronized UInteger readUint32(int offset) {
        return UInteger.valueOf(readInt(offset, 4));
    }

    public synchronized void writeUint8(UByte value) {
        ensureCapacity(mOffset + 1);
        mBuffer[mOffset++] = value.byteValue();
    }

    public synchronized void writeUint16(UShort value) {
        writeInt(value.longValue(), 2);
    }

    public synchronized void writeUint32(UInteger value) {
        writeInt(value.longValue(), 4);
    }

    public synchronized void writeUint64(ULong value) {
        writeInt(value.longValue(), 8);
    }

    public synchronized void writeInt16(Short value) {
        writeInt(value.longValue(), 2);
    }

    public synchronized void writeInt32(Integer value) {
        writeInt(value.longValue(), 4);
    }

    public synchronized void writeInt64(Long value) {
        writeInt(value, 8);
    }

    public synchronized void writeVarint32(int value) {
        ensureCapacity(mOffset + 5);
        while (value >= 0x80) {
            mBuffer[mOffset++] = (byte) ((value & 0x7F) | 0x80);
            value >>= 7;
        }
        mBuffer[mOffset++] = (byte) value;
    }

    public synchronized void writeVString(String value) {
        try {
            byte[] bytes = value.getBytes("UTF-8");
            writeVarint32(bytes.length);
            write(bytes);
        } catch (Exception e) {
        }
    }

    public synchronized void writeString(String value) {
        try {
            write(value.getBytes("UTF-8"));
        } catch (Exception e) {
        }
    }
}
