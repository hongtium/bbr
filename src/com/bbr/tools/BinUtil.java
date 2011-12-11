package com.bbr.tools;
/**
 * byte数组和整型数据的交互操作类
 * User: chenjx
 * Date: 2011-6-4
 */
public class BinUtil {
    public static void putInt(byte[] bb, int x, int index) {
        bb[index + 0] = (byte) (x >> 24);
        bb[index + 1] = (byte) (x >> 16);
        bb[index + 2] = (byte) (x >> 8);
        bb[index + 3] = (byte) (x >> 0);
    }
    public static int getInt(byte[] bb, int index) {
        return (int) ((((bb[index + 0] & 0xff) << 24)
                | ((bb[index + 1] & 0xff) << 16)
                | ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
    }

    public static void putLong(byte[] bb, long x, int index) {
        bb[index + 0] = (byte) (x >> 56);
        bb[index + 1] = (byte) (x >> 48);
        bb[index + 2] = (byte) (x >> 40);
        bb[index + 3] = (byte) (x >> 32);
        bb[index + 4] = (byte) (x >> 24);
        bb[index + 5] = (byte) (x >> 16);
        bb[index + 6] = (byte) (x >> 8);
        bb[index + 7] = (byte) (x >> 0);
    }
    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[index + 0] & 0xff) << 56)
                | (((long) bb[index + 1] & 0xff) << 48)
                | (((long) bb[index + 2] & 0xff) << 40)
                | (((long) bb[index + 3] & 0xff) << 32)
                | (((long) bb[index + 4] & 0xff) << 24)
                | (((long) bb[index + 5] & 0xff) << 16)
                | (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
    }

    public static int setFlag(int flag, int idx, int bits, int value) {
        int value_of_bits = (1 << bits) - 1;
        return (flag & ~(value_of_bits << idx) | (-1 & (value << idx)));
    }

    public static int getFlag(int flag, int idx, int bits) {
        long value_of_bits = (1 << bits) - 1;
        return (int) ( (flag >> idx) & value_of_bits );
    }


    /**
     * 通用的设置long变量的各个标志位的方法
     * @param status	原status
     * @param idx		需要设置的起始位号,从低到高，从0开始，最大63
     * @param bits		本段设置项的 位长度
     * @param value		本段设置项的 要设置的新值
     * @return			设置后的新值
     */
    public static long setStatus(long status, int idx, int bits, int value) {
        long value_of_bits = (1L << bits) - 1;
        return (status & ~(value_of_bits << idx) | (-1L & (value << idx)));
    }

    /**
     * 通用的获取 long变量的 某段标志位的方法
     * @param status	原status
     * @param idx		需要设置的起始位号,从低到高，从0开始，最大63
     * @param bits		本段设置项的 位长度
     * @return			该段位的结果代表值
     */
    public static int getStatus(long status, int idx, int bits) {
        long value_of_bits = (1L << bits) - 1;
        return (int) ( (status >> idx) & value_of_bits );
    }

    public static long longMemcpy(byte[] buf, int offset, int len) {
        long ret = 0;

        if (len > 8)
            return 0;
        try {
            if (len >= 1)
                ret |= (buf[offset++] & 0xFF);
            if (len >= 2)
                ret |= ((buf[offset++] << 8) & 0xFF00);
            if (len >= 3)
                ret |= ((buf[offset++] << 16) & 0xFF0000);
            if (len >= 4)
                ret |= ((buf[offset++] << 24) & 0xFF000000L);
            if (len >= 5)
                ret |= (((long) buf[offset++] << 32) & 0xFF00000000L);
            if (len >= 6)
                ret |= (((long) buf[offset++] << 40) & 0xFF0000000000L);
            if (len >= 7)
                ret |= (((long) buf[offset++] << 48) & 0xFF000000000000L);
            if (len == 8)
                ret |= (((long) buf[offset++] << 56) & 0xFF00000000000000L);
        } catch (Exception E) {
        }
        return ret;
    }

}
