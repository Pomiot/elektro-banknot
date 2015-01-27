package pl.edu.amu.wmi.common.protocols.blindSignature;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import pl.edu.amu.wmi.common.cryptography.RSA;

public class RsaBlind {

    private final RSA Rsa;
    private final RSAPublicKey rsaPublicKey;
//    Math elements to calculate
    private BigInteger m;
    private BigInteger e;
    private BigInteger d;
    private BigInteger r;
    private BigInteger n;
    private BigInteger gcd;
    private BigInteger one;

    public RsaBlind(RSAPublicKey rsaPublicKey) {
        this.Rsa = null;
        this.rsaPublicKey = rsaPublicKey;
        this.one = new BigInteger("1"); // IMPORTANT TO CHECK ESTATMENT
        this.m = null; //SIZE OF MESSAGE TODO: CODE FOR THIS
        this.e = null; //FROM PUBLIC KEY RSA
        this.d = null; // FROM PRIVATE KEY RSA
        this.r = null;
        this.n = null; //MODULE FROM RSA
        this.gcd = null; //
        System.out.println(this.rsaPublicKey.getAlgorithm());
        System.out.println(Arrays.toString(this.rsaPublicKey.getEncoded()));
    }

    public RsaBlind(RSA Rsa) {
        this.one = new BigInteger("1"); // IMPORTANT TO CHECK ESTATMENT
        this.m = null; //SIZE OF MESSAGE TODO: CODE FOR THIS
        this.e = null; //FROM PUBLIC KEY RSA
        this.d = null; // FROM PRIVATE KEY RSA
        this.r = null;
        this.n = null; //MODULE FROM RSA
        this.gcd = null; //
        this.Rsa = Rsa;
        this.rsaPublicKey = null;

    }

    public RSA getRsa() {
        return Rsa;
    }

    public byte[] getR() {
        return r.toByteArray();
    }

    public void prepareBlind() {
        if (this.Rsa != null) {
            if (this.Rsa.getPublicKey() != null) {
                this.e = this.Rsa.getPublicKey().getPublicExponent();
                this.n = this.Rsa.getPublicKey().getModulus();
            }
            if (this.Rsa.getPrivateKey() != null) {
                this.d = this.Rsa.getPrivateKey().getPrivateExponent();
                this.n = this.Rsa.getPrivateKey().getModulus();
            }
        } else {
            if (this.rsaPublicKey != null) {
                this.e = this.rsaPublicKey.getPublicExponent();
                this.n = this.rsaPublicKey.getModulus();
            } else {
                System.out.println("NO RSA ELEMENTS");
            }
        }
    }

    public void generateNewRandom() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] randomBytes = new byte[this.n.toByteArray().length];
            do {
                random.nextBytes(randomBytes);
                this.r = new BigInteger(randomBytes);
                gcd = this.r.gcd(n);
            } while (!gcd.equals(one) || this.r.compareTo(n) >= 0 || this.r.compareTo(one) <= 0);
            System.out.println("random size" + this.r.toByteArray().length);
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(RsaBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] blind(byte[] message) {
        byte[] raw = message;
        m = new BigInteger(raw);
        try {
            BigInteger b = ((this.r.modPow(e, n)).multiply(m)).mod(n);
            return b.toByteArray();
        } catch (Exception e) {

        }
        return null;

    }
//METHOD SIGN BLIND MESSAGE

    public byte[] sign(byte[] blindedmessage) {
        BigInteger b = new BigInteger(blindedmessage);
        BigInteger bs = b.modPow(d, n);
        System.out.println("bs = " + bs);
        return bs.toByteArray();
    }

    public byte[] unblind(byte[] blindedmessage) {
        BigInteger bs = new BigInteger(blindedmessage);
        BigInteger s = this.r.modInverse(n).multiply(bs).mod(n);
        return s.toByteArray();
    }

    public void verify(String message, BigInteger unblind) throws UnsupportedEncodingException {
        byte[] raw = message.getBytes("UTF8");
        m = new BigInteger(raw);

        BigInteger sig_m = m.modPow(d, n);
        System.out.println(sig_m);

        System.out.println(unblind.equals(sig_m));

        BigInteger check = unblind.modPow(e, n);
        System.out.println(m.equals(check));

    }

    public byte[] blockBlind(byte[] bytes) {
        int length = this.n.toByteArray().length;
        byte[] scrambled = new byte[0];
        byte[] toReturn = new byte[0];
        byte[] buffer = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            if ((i > 0) && (i % length == 0)) {
                scrambled = this.blind(buffer);

                toReturn = append(toReturn, scrambled);
                int newlength = length;
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                buffer = new byte[newlength];
            }
            buffer[i % length] = bytes[i];
        }
        System.out.println(buffer.length);
        scrambled = this.blind(buffer);

        toReturn = append(toReturn, scrambled);

        return toReturn;
    }

    public byte[] blockUnblind(byte[] bytes) {
        int length = 128;
        byte[] scrambled = new byte[0];
        byte[] toReturn = new byte[0];
        byte[] buffer = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            if ((i > 0) && (i % length == 0)) {
                do {
                    scrambled = this.unblind(buffer);
                    if (scrambled.length > 128) {
                        this.prepareBlind();
                    }
                } while (!(scrambled.length == 128));

                toReturn = append(toReturn, scrambled);
                int newlength = length;
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                buffer = new byte[newlength];
            }
            buffer[i % length] = bytes[i];
        }

        scrambled = this.unblind(buffer);

        toReturn = append(toReturn, scrambled);

        return toReturn;
    }

    private byte[] append(byte[] prefix, byte[] suffix) {
        byte[] toReturn = new byte[prefix.length + suffix.length];
        for (int i = 0; i < prefix.length; i++) {
            toReturn[i] = prefix[i];
        }
        for (int i = 0; i < suffix.length; i++) {
            toReturn[i + prefix.length] = suffix[i];
        }
        return toReturn;
    }
}
