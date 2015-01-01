package pl.edu.amu.wmi.common.cryptography;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

public class RSA implements RSAConfiguration{

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public void GeneratePairKey() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(CRYPTO_ALGORITHM);
        kpg.initialize(512);
        KeyPair kp = kpg.generateKeyPair();
        this.publicKey = kp.getPublic();
        this.privateKey = kp.getPrivate();
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    
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
