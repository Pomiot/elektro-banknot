package pl.edu.amu.wmi.common.objects;

/**
 * Klasa stanaowiąca banknot amount - kwota banknotu uniquenessString -
 * identyfikator banknotu identyfikator jest byte[] wynika to tego ze
 * SecureRandom działa na tym
 *
 */
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.edu.amu.wmi.common.Util.util;
import pl.edu.amu.wmi.common.protocols.hashCommitmentscheme.HashCommitmentScheme;
import pl.edu.amu.wmi.common.protocols.secretSharing.secretSharing;

/**
 * Created by Tomasz on 2015-01-15. Modification by Patryk on 2015-01-19
 *
 */
public class Banknote implements Serializable {

    private String amount;
    private byte[] uniquenessString;

    //pola tymczasowe chyba
    private byte[] leftIdBanknoteFromIdCustomer;
    private byte[] rightIdBanknoteFromIdCustomer;

    private byte[] leftIdBanknoteFromIdCustomerRandom1;
    private byte[] leftIdBanknoteFromIdCustomerRandom2;
    private byte[] leftIdBanknoteFromIdCustomerHash;

    private byte[] rightIdBanknoteFromIdCustomerRandom1;
    private byte[] rightIdBanknoteFromIdCustomerRandom2;
    private byte[] rightIdBanknoteFromIdCustomerHash;

    public Banknote(String amount, byte[] customerId) {
        this.amount = amount;
        this.generateUniquenessString();
        System.out.println(">---Customer ID: " + Arrays.toString(customerId));
        this.generateCustomerIdInBanknote(customerId);
        this.generateHashCommitmentCustomerIdInBanknote();
        this.showAll();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String value) {
        this.amount = value;
    }

    public byte[] getUniquenessString() {
        return uniquenessString;
    }

    //Generuje identyfikator banknotu
    private void generateUniquenessString() {
        this.uniquenessString = util.generateSecureRandom(16);
    }

    //Funkcja generująca pojedynczy identyfikator banknotu
    //przy uzyciu Identyfikatora klienta
    //zwraca pare liczb
    private void generateCustomerIdInBanknote(byte[] customerId) {
        secretSharing secretId = new secretSharing(customerId);
        try {
            secretId.generateSecretSharing();
            this.leftIdBanknoteFromIdCustomer = secretId.getRandom1();
            this.rightIdBanknoteFromIdCustomer = secretId.getResult();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(Banknote.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void generateHashCommitmentCustomerIdInBanknote() {
        //Generowanie zobowiazania do lewej strony
        HashCommitmentScheme leftIdBanknoteFromIdCustomerCommitmentScheme
                = new HashCommitmentScheme(this.leftIdBanknoteFromIdCustomer);
        this.leftIdBanknoteFromIdCustomerRandom1
                = leftIdBanknoteFromIdCustomerCommitmentScheme.getRandom1();
        this.leftIdBanknoteFromIdCustomerRandom2
                = leftIdBanknoteFromIdCustomerCommitmentScheme.getRandom2();
        this.leftIdBanknoteFromIdCustomerHash
                = leftIdBanknoteFromIdCustomerCommitmentScheme.getHash();
        //Generowanie zoobowiazania do prawej strony
        HashCommitmentScheme rightIdBanknoteFromIdCustomerCommitmentScheme
                = new HashCommitmentScheme(this.rightIdBanknoteFromIdCustomer);
        this.rightIdBanknoteFromIdCustomerRandom1
                = rightIdBanknoteFromIdCustomerCommitmentScheme.getRandom1();
        this.rightIdBanknoteFromIdCustomerRandom2
                = rightIdBanknoteFromIdCustomerCommitmentScheme.getRandom2();
        this.rightIdBanknoteFromIdCustomerHash
                = rightIdBanknoteFromIdCustomerCommitmentScheme.getHash();
    }
    private void showAll(){
        System.out.println("leftIdBanknoteFromIdCustomer: "+Arrays.toString(this.leftIdBanknoteFromIdCustomer));
        System.out.println("rightIdBanknoteFromIdCustomer: "+ Arrays.toString(this.rightIdBanknoteFromIdCustomer));
        System.out.println("leftIdBanknoteFromIdCustomerRandom1: "+Arrays.toString(this.leftIdBanknoteFromIdCustomerRandom1));
        System.out.println("leftIdBanknoteFromIdCustomerRandom2: "+Arrays.toString(this.leftIdBanknoteFromIdCustomerRandom2));
        System.out.println("leftIdBanknoteFromIdCustomerHash: "+Arrays.toString(this.leftIdBanknoteFromIdCustomerHash));
        System.out.println("rightIdBanknoteFromIdCustomerRandom1: "+Arrays.toString(this.rightIdBanknoteFromIdCustomerRandom1));
        System.out.println("rightIdBanknoteFromIdCustomerRandom2: "+Arrays.toString(this.rightIdBanknoteFromIdCustomerRandom2));
        System.out.println("rightIdBanknoteFromIdCustomerHash: "+Arrays.toString(this.rightIdBanknoteFromIdCustomerHash));
    }
}
