package pl.edu.amu.wmi.bank;

import sun.security.rsa.RSAKeyPairGenerator;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Created by Tomasz on 2015-01-21.
 */
public class Keys {

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyPair keyPair;

    public Keys() {

        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        rsaKeyPairGenerator.initialize(512, new SecureRandom());
        KeyPair keyPair = rsaKeyPairGenerator.generateKeyPair();

        setPublicKey(keyPair.getPublic());
        setPrivateKey(keyPair.getPrivate());
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
