package pl.edu.amu.wmi.common.cryptography;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA implements RSAConfiguration {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSA() {
        this.publicKey = null;
        this.privateKey = null;
    }

    public void GeneratePairKey() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(CRYPTO_ALGORITHM);
        kpg.initialize(1024);
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

    public byte[] EncryptTransfer(byte[] sentenceBytes) throws Exception {
        Cipher cipher = Cipher.getInstance(MASTERKEY_CIPHER);
        byte[] encrypted = blockCipher(sentenceBytes, Cipher.ENCRYPT_MODE);
        return encrypted;
    }

    public byte[] DecryptTransfer(byte[] sentenceBytes) throws Exception {
        Cipher cipher = Cipher.getInstance(MASTERKEY_CIPHER);
        byte[] decrypted = blockCipher(sentenceBytes, Cipher.DECRYPT_MODE);
        return decrypted;
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

    private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA");
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(mode, publicKey);
        } else {
            cipher.init(mode, privateKey);
        }
        byte[] scrambled = new byte[0];
        byte[] toReturn = new byte[0];
        int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;
        byte[] buffer = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            if ((i > 0) && (i % length == 0)) {
                scrambled = cipher.doFinal(buffer);
                toReturn = append(toReturn, scrambled);
                int newlength = length;
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                buffer = new byte[newlength];
            }
            buffer[i % length] = bytes[i];
        }

        scrambled = cipher.doFinal(buffer);

        toReturn = append(toReturn, scrambled);

        return toReturn;
    }
}
