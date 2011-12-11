package com.bbr.tools;

public class ByteArrayUtil {

	public static void put(byte[] source, byte[] target, int offset) {
		System.arraycopy(source, 0, target, offset, source.length);
	}

	public static byte[] get(byte[] array, int offset) {
		return get(array, offset, array.length - offset);
	}

	public static byte[] get(byte[] array, int offset, int length) {
		byte[] result = new byte[length];
		System.arraycopy(array, offset, result, 0, length);
		return result;
	}

	public static void putShort(short value, byte[] array, int offset){
		array[offset]	= (byte)(0xff & (value >> 8));
		array[offset+1]	= (byte)(0xff & value);
	}

	@SuppressWarnings("cast")
	public static short getShort(byte[] array, int offset){
		return
			(short)(((array[offset]   & 0xff) << 8) |
					((array[offset+1])& 0xff));
	}

	public static void putInt(int value, byte[] array, int offset) {
		array[offset]   = (byte)(0xff & (value >> 24));
		array[offset+1] = (byte)(0xff & (value >> 16));
		array[offset+2] = (byte)(0xff & (value >> 8));
		array[offset+3] = (byte)(0xff & value);
	}

	public static int getInt(byte[] array, int offset) {
		return
			((array[offset]   & 0xff) << 24) |
			((array[offset+1] & 0xff) << 16) |
			((array[offset+2] & 0xff) << 8) |
			 (array[offset+3] & 0xff);
	}

	public static void putLong(long value, byte[] array, int offset) {
		array[offset]   = (byte)(0xff & (value >> 56));
		array[offset+1] = (byte)(0xff & (value >> 48));
		array[offset+2] = (byte)(0xff & (value >> 40));
		array[offset+3] = (byte)(0xff & (value >> 32));
		array[offset+4] = (byte)(0xff & (value >> 24));
		array[offset+5] = (byte)(0xff & (value >> 16));
		array[offset+6] = (byte)(0xff & (value >> 8));
		array[offset+7] = (byte)(0xff & value);
	}

	@SuppressWarnings("cast")
    public static long getLong(byte[] array, int offset) {
		return
			((long)(array[offset]   & 0xff) << 56) |
			((long)(array[offset+1] & 0xff) << 48) |
			((long)(array[offset+2] & 0xff) << 40) |
			((long)(array[offset+3] & 0xff) << 32) |
			((long)(array[offset+4] & 0xff) << 24) |
			((long)(array[offset+5] & 0xff) << 16) |
			((long)(array[offset+6] & 0xff) << 8) |
			((long)(array[offset+7] & 0xff));
	}

	public static boolean matchesPattern(byte[] value, byte[] mask, byte[] pattern) {
		for (int i = 0; i < value.length; i++) {
			if ( ((value[i] ^ pattern[i]) & mask[i]) != 0) {
				return false;
			}
		}

		return true;
	}

	public static boolean regionMatches(byte[] subValue, byte[] superValue, int offset) {
		for (int i = 0; i < subValue.length; i++) {
			if (subValue[i] != superValue[i+offset]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Compares two regions of bytes, indicating whether one is larger than the
	 * other.
	 *
	 * @param array1 The first byte array.
	 * @param startIdx1 The start of the region in the first array.
	 * @param array2 The second byte array.
	 * @param startIdx2 The start of the region in the second array.
	 * @param length The length of the region that should be compared.
	 * @return A negative number when the first region is smaller than the
	 * second, a positive number when the first region is larger than the
	 * second, or 0 if the regions are equal.
	 */
	public static int compareRegion(byte[] array1, int startIdx1, byte[] array2, int startIdx2, int length) {
		int result = 0;
		for (int i = 0; result == 0 && i < length; i++) {
			result = (array1[startIdx1 + i] & 0xff) - (array2[startIdx2 + i] & 0xff);
		}
		return result;
	}

	/**
	 * Returns the hexadecimal value of the supplied byte array. The resulting
	 * string always uses two hexadecimals per byte. As a result, the length
	 * of the resulting string is guaranteed to be twice the length of the
	 * supplied byte array.
	 */
	public static String toHexString(byte[] array) {
		return StringUtil.byte2HexString(array);
	}
}
