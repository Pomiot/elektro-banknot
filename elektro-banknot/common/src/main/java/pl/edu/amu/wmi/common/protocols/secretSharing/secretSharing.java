package pl.edu.amu.wmi.common.protocols.secretSharing;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;

public class secretSharing {

    private String message;
    private byte[] raw;
    private byte[] result;
    
    
    public secretSharing(String message) {
        this.message = message;
    }

    @SuppressWarnings("empty-statement")
    public void generateSecretSharing() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException {
        //this.raw = message.getBytes("UTF8");
        this.raw = new byte[]{0,0,0,0};
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        System.out.println("Dlugosc raw:"+this.raw.length);
        //byte[] r1 = new byte[this.raw.length];
        byte[] r1 = new byte[]{1,1,1,1};
        System.out.println("Dlugosc r1:"+r1.length);
        //random.nextBytes(r1);
        System.out.println(Arrays.toString(r1));
        this.result = new byte[this.raw.length];
        int i=0;
        for (byte b: this.raw){
            this.result[i]=(byte) (b^r1[i]);
            i++;
        }
        System.out.println("Wyjscie:"+Arrays.toString(this.result));
        
        
    }
}
