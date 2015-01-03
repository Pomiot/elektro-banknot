package pl.edu.amu.wmi.common.protocols.blindSignature;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;
import pl.edu.amu.wmi.common.cryptography.RSA;

public class RsaBlind {

    private final RSA Rsa;
//    Math elements to calculate
    private BigInteger m;
    private BigInteger e;
    private BigInteger d;
    private BigInteger r;
    private BigInteger n;
    private BigInteger gcd;
    private BigInteger one;

    public RsaBlind(RSA Rsa) {
        this.one = new BigInteger("1"); // IMPORTANT TO CHECK ESTATMENT
        this.m = null; //SIZE OF MESSAGE TODO: CODE FOR THIS
        this.e = null; //FROM PUBLIC KEY RSA
        this.d = null; // FROM PRIVATE KEY RSA
        this.r = null;
        this.n = null; //MODULE FROM RSA
        this.gcd = null; //
        this.Rsa = Rsa;
        System.out.println(this.Rsa.getPublicKey().getAlgorithm());
        System.out.println(Arrays.toString(this.Rsa.getPublicKey().getEncoded()));
    }

    public RSA getRsa() {
        return Rsa;
    }

    private void prepareBlind() {
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
            System.out.println("NO RSA ELEMENTS");
        }
    }

    public BigInteger blind(String message) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
        prepareBlind();
        byte[] raw = message.getBytes("UTF8");
        m = new BigInteger(raw);
        
        //PREPARE R number
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] randomBytes = new byte[10];
        do {
            random.nextBytes(randomBytes);
            r = new BigInteger(randomBytes);
            gcd = r.gcd(n);
            System.out.println("gcd: "+gcd);
        } while (!gcd.equals(one)|| r.compareTo(n)>=0 ||r.compareTo(one)<=0);
        
        BigInteger b = ((r.modPow(e, n)).multiply(m)).mod(n);
        System.out.println("b: "+b);
        return b;
        
    }
//METHOD SIGN BLIND MESSAGE
    public BigInteger sign(BigInteger b) {
        prepareBlind();
        BigInteger bs = b.modPow(d, n);
        System.out.println("bs = "+bs);
        return bs;
    }

    public BigInteger unblind(BigInteger bs) {
        BigInteger s = r.modInverse(n).multiply(bs).mod(n);
        System.out.println("s: "+s);
        return s;
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
}
