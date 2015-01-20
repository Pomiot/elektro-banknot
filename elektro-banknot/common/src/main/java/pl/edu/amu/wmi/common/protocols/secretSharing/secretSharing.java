package pl.edu.amu.wmi.common.protocols.secretSharing;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import pl.edu.amu.wmi.common.Util.util;

public class secretSharing {

    private String message;
    private byte[] byteMessage;
    private byte[] result;
    private byte[] random1;

    public secretSharing(String message) {
        this.message = message;
    }

    public secretSharing(byte[] byteMessage) {
        this.message = new String();
        this.byteMessage = byteMessage;

    }

    @SuppressWarnings("empty-statement")
    public void generateSecretSharing() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException {
        if (!this.message.isEmpty()) {
            this.byteMessage = message.getBytes("UTF8");
        }
        this.random1 = util.generateSecureRandom(this.byteMessage.length);
        this.result = new byte[this.byteMessage.length];
        int i = 0;
        for (byte b : this.byteMessage) {
            this.result[i] = (byte) (b ^ this.random1[i]);
            i++;
        }
    }

    public byte[] getRandom1() {
        return random1;
    }

    public byte[] getResult() {
        return result;
    }

}
