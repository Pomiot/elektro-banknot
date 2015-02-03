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

    public secretSharing() {
        this.message = null;
        this.byteMessage = null;
        this.random1 = null;
        this.result= null;
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
        do  {
            this.random1 = util.generateSecureRandom(this.byteMessage.length);
        } while(this.random1.length != this.byteMessage.length);
        this.result = new byte[this.byteMessage.length];
        int i = 0;
        for (byte b : this.byteMessage) {
            this.result[i] = (byte) (b ^ this.random1[i]);
            i++;
        }
    }
    public void generateMessage(){
        this.byteMessage = new byte[this.result.length];
        int i=0;
        for(byte b: this.result){
            this.byteMessage[i] = (byte) (b ^this.random1[i]);
            i++;
        }
    }

    public void setRandom1(byte[] random1) {
        this.random1 = random1;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public byte[] getByteMessage() {
        return byteMessage;
    }
    
    public byte[] getRandom1() {
        return random1;
    }

    public byte[] getResult() {
        return result;
    }

}
