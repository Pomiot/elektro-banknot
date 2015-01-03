package pl.edu.amu.wmi.common.cryptography;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class RSA implements RSAConfiguration{

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSA() {
        this.publicKey = null;
        this.privateKey = null;
    }
    
    public void GeneratePairKey() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(CRYPTO_ALGORITHM);
        kpg.initialize(512);
        KeyPair kp = kpg.generateKeyPair();
        this.publicKey = (RSAPublicKey) kp.getPublic();
        this.privateKey = (RSAPrivateKey) kp.getPrivate();
    }
    public RSAPublicKey getPublicKey() {
        return this.publicKey;
    }
    public RSAPrivateKey getPrivateKey() {
        return this.privateKey;
    }
    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }
    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }
//   TODO: PROPABLY ITS MUST CHANGE FOR DIFFRENT KEYS 
    public byte[] EncryptTransfer(byte[] sentenceBytes) throws Exception{
        Cipher cipher = Cipher.getInstance(MASTERKEY_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(sentenceBytes);
    }
    
    public byte[] DecryptTransfer(byte[] sentenceBytes) throws Exception{
        Cipher cipher = Cipher.getInstance(MASTERKEY_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(sentenceBytes);
    }
}
