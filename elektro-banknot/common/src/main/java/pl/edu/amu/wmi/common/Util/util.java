package pl.edu.amu.wmi.common.Util;

public class util {

    private final static char[] hexChars = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String byteArray2Hex(byte[] ba) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ba.length; i++) {
            int hbits = (ba[i] & 0x000000f0) >> 4;
            int lbits = ba[i] & 0x0000000f;
            sb.append("").append(hexChars[hbits]).append(hexChars[lbits]);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
