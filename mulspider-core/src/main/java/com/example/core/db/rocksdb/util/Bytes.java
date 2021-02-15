package com.example.core.db.rocksdb.util;

import java.nio.charset.StandardCharsets;

public class Bytes {
    /**
     * Transforms a long value to the platform-specific
     * byte representation.
     *
     * @param value numeric value.
     * @return platform-specific long value.
     */
    public static byte[] longToByte(long value) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((value >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * Transforms platform specific byte representation
     * to long value.
     *
     * @param data platform-specific byte array.
     * @return numeric value.
     */
    public static long byteToLong(byte[] data) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (data[ix] & 0xff);
        }
        return num;
    }

    public static byte[] stringToBytes(String key) {
        if (key == null) {
            return null;
        }
        return key.getBytes(StandardCharsets.UTF_8);
    }

    public static String bytesToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Utility constructor
     */
    private Bytes() {
    }
}
