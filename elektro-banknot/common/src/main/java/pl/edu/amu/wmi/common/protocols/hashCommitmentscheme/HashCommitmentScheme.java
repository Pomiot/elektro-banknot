/**
 * Zobowiazanie bitowe z funkcją hashującą
 */
package pl.edu.amu.wmi.common.protocols.hashCommitmentscheme;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.edu.amu.wmi.common.Util.util;

/**
 *
 * @author Patryk
 */
public class HashCommitmentScheme {

    private final static int SIZE_OF_RANDOM = 16;

    private byte[] random1;
    private byte[] random2;
    private byte[] decision;
    private byte[] hash;

    public byte[] getRandom1() {
        return random1;
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getRandom2() {
        return random2;
    }
    
    //Konstruktor do tworzenia zobowiazania
    public HashCommitmentScheme(byte[] bytesDecision) {
        this.decision = bytesDecision;
        this.generateRandoms();
        this.hash = this.generateHash();
    }
    
    //Konstruktor zoobowiazania do weryfikacji
    public HashCommitmentScheme(byte[] random1ToVer, byte[] hashToVer) {
        this.random1 = random1ToVer;
        this.hash = hashToVer;
    }

    private void generateRandoms() {
        this.random1 = util.generateSecureRandom(SIZE_OF_RANDOM);
        this.random2 = util.generateSecureRandom(SIZE_OF_RANDOM);
    }

    private byte[] generateHash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(this.random1);
            md.update(this.random2);
            md.update(this.decision);
            return md.digest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashCommitmentScheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Boolean compareHashCommitmentScheme(HashCommitmentScheme hashCommitmentScheme){
        return (this.random1 == hashCommitmentScheme.getRandom1()) && (this.hash == hashCommitmentScheme.getHash());
    }
}
