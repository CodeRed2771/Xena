package org.usfirst.frc.team2771.libs;

/**
 * The PrimitiveSerializer takes data and turns it into byte arrays, or visa
 * versa. All data sent over the network is serialized into byte arrays.
 *
 * @author Austin Wheeler
 * @since Dash 3.0
 */
public class PrimitiveSerializer {

    private PrimitiveSerializer() {
    }

    public static short bytesToShort(byte[] arr) {
        short s = (short) (((short) (arr[0] & 0xff))
                | (((short) (arr[1] & 0xff)) << 8));
        return s;
    }

    public static int bytesToInt(byte[] arr) {
        int i = ((int) (arr[0] & 0xff))
                | (((int) (arr[1] & 0xff)) << 8)
                | (((int) (arr[2] & 0xff)) << 16)
                | (((int) (arr[3] & 0xff)) << 24);
        return i;
    }

    public static long bytesToLong(byte[] arr) {
        long l = ((long) (arr[0] & 0xff))
                | (((long) (arr[1] & 0xff)) << 8)
                | (((long) (arr[2] & 0xff)) << 16)
                | (((long) (arr[3] & 0xff)) << 24)
                | (((long) (arr[4] & 0xff)) << 32)
                | (((long) (arr[5] & 0xff)) << 40)
                | (((long) (arr[6] & 0xff)) << 48)
                | (((long) (arr[7] & 0xff)) << 56);
        return l;
    }

    public static float bytesToFloat(byte[] arr) {
        return Float.intBitsToFloat(bytesToInt(arr));
    }

    public static double bytesToDouble(byte[] arr) {
        return Double.longBitsToDouble(bytesToLong(arr));
    }

    public static char bytesToChar(byte[] arr) {
        char c = (char) (((char) (arr[0] & 0xff))
                | (((char) (arr[1] & 0xff)) << 8));
        return c;
    }

    public static boolean bytesToBoolean(byte[] arr) {
        return arr[0] != 0x00;
    }

    public static String bytesToString(byte[] arr) {
        StringBuilder sb = new StringBuilder(arr.length / 2);
        for (int i = 0; i < arr.length; i += 2) {
            sb.append((char) (((char) (arr[i] & 0xff))
                    | (((char) (arr[i + 1] & 0xff)) << 8)));
        }
        return sb.toString();
    }

    public static byte[] toByteArray(short s) {
        return new byte[]{
            (byte) ((s) & 0xff),
            (byte) ((s >> 8) & 0xff)};
    }

    public static byte[] toByteArray(int i) {
        return new byte[]{
            (byte) ((i) & 0xff),
            (byte) ((i >> 8) & 0xff),
            (byte) ((i >> 16) & 0xff),
            (byte) ((i >> 24) & 0xff)};
    }

    public static byte[] toByteArray(long l) {
        return new byte[]{
            (byte) ((l) & 0xff),
            (byte) ((l >> 8) & 0xff),
            (byte) ((l >> 16) & 0xff),
            (byte) ((l >> 24) & 0xff),
            (byte) ((l >> 32) & 0xff),
            (byte) ((l >> 40) & 0xff),
            (byte) ((l >> 48) & 0xff),
            (byte) ((l >> 56) & 0xff)};
    }

    public static byte[] toByteArray(float f) {
        return toByteArray(Float.floatToIntBits(f));
    }

    public static byte[] toByteArray(double d) {
        return toByteArray(Double.doubleToLongBits(d));
    }

    public static byte[] toByteArray(char c) {
        return new byte[]{
            (byte) ((c) & 0xff),
            (byte) ((c >> 8) & 0xff)};
    }

    public static byte[] toByteArray(boolean b) {
        if (b) {
            return new byte[]{0x01};
        } else {
            return new byte[]{0x00};
        }
    }

    public static byte[] toByteArray(String s) {
        byte[] b = new byte[s.length() * 2];
        for (int i = 0; i < s.length(); i++) {
            b[i * 2] = (byte) ((s.charAt(i)) & 0xff);
            b[(i * 2) + 1] = (byte) ((s.charAt(i) >> 8) & 0xff);
        }
        return b;
    }

    public static byte[] toByteArrayWithLength(String s) {
        byte[] b = new byte[(s.length() * 2) + 4];
        b[0] = (byte) ((s.length()) & 0xff);
        b[1] = (byte) ((s.length() >> 8) & 0xff);
        b[2] = (byte) ((s.length() >> 16) & 0xff);
        b[3] = (byte) ((s.length() >> 24) & 0xff);
        for (int i = 0; i < s.length(); i++) {
            b[(i * 2) + 4] = (byte) ((s.charAt(i)) & 0xff);
            b[(i * 2) + 5] = (byte) ((s.charAt(i) >> 8) & 0xff);
        }
        return b;
    }
}
