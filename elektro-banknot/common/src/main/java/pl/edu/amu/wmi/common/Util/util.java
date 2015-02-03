package pl.edu.amu.wmi.common.Util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pl.edu.amu.wmi.common.objects.BanknoteIface.lengthUniquenessString;
import pl.edu.amu.wmi.common.protocols.blindSignature.RsaBlind;

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

    //Metoda generująca random określonej długości

    public static byte[] generateSecureRandom(int size) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            BigInteger r;
            BigInteger one = new BigInteger("1");
            BigInteger gcd;
            byte[] randomBytes = new byte[size];
            do {
                random.nextBytes(randomBytes);
                r = new BigInteger(randomBytes);
            } while (r.compareTo(one) <= 0);
            return r.toByteArray();
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(RsaBlind.class.getName()).log(Level.SEVERE, null, ex);
        }

//        try {
//            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
//            byte[] aesKey = new byte[size];
//            random.nextBytes(aesKey);       
//            return aesKey;
//        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
//            Logger.getLogger(util.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return null;
    }
}
