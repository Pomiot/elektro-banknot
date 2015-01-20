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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private List<byte[]> leftIdBanknoteFromIdCustomerList = new ArrayList<>();
    private byte[] rightIdBanknoteFromIdCustomer;
    private List<byte[]> rightIdBanknoteFromIdCustomerList = new ArrayList<>();

    private byte[] leftIdBanknoteFromIdCustomerRandom1;
    private List<byte[]> leftIdBanknoteFromIdCustomerRandom1List = new ArrayList<>();
    private byte[] leftIdBanknoteFromIdCustomerRandom2;
    private List<byte[]> leftIdBanknoteFromIdCustomerRandom2List = new ArrayList<>();
    private byte[] leftIdBanknoteFromIdCustomerHash;
    private List<byte[]> leftIdBanknoteFromIdCustomerHashList = new ArrayList<>();

    private byte[] rightIdBanknoteFromIdCustomerRandom1;
    private List<byte[]> rightIdBanknoteFromIdCustomerRandom1List = new ArrayList<>();
    private byte[] rightIdBanknoteFromIdCustomerRandom2;
    private List<byte[]> rightIdBanknoteFromIdCustomerRandom2List = new ArrayList<>();
    private byte[] rightIdBanknoteFromIdCustomerHash;
    private List<byte[]> rightIdBanknoteFromIdCustomerHashList = new ArrayList<>();

    public Banknote(String amount, byte[] customerId) {
        this.amount = amount;
        this.generateUniquenessString();
        System.out.println(">---Customer ID: " + Arrays.toString(customerId));
        this.generateCustomerIdInBanknote(customerId);
//        this.generateHashCommitmentCustomerIdInBanknote();
        //this.showAll();
        this.showLeftIdBanknoteFromIdCustomer();
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
        for (int i = 0; i < 100; i++) {
            secretSharing secretId = new secretSharing(customerId);
            try {
                secretId.generateSecretSharing();
                this.leftIdBanknoteFromIdCustomerList.add(secretId.getRandom1());
                this.rightIdBanknoteFromIdCustomerList.add(secretId.getResult());
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchProviderException ex) {
                Logger.getLogger(Banknote.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private void showAll() {
        System.out.println("leftIdBanknoteFromIdCustomer: " + Arrays.toString(this.leftIdBanknoteFromIdCustomer));
        System.out.println("rightIdBanknoteFromIdCustomer: " + Arrays.toString(this.rightIdBanknoteFromIdCustomer));
        System.out.println("leftIdBanknoteFromIdCustomerRandom1: " + Arrays.toString(this.leftIdBanknoteFromIdCustomerRandom1));
        System.out.println("leftIdBanknoteFromIdCustomerRandom2: " + Arrays.toString(this.leftIdBanknoteFromIdCustomerRandom2));
        System.out.println("leftIdBanknoteFromIdCustomerHash: " + Arrays.toString(this.leftIdBanknoteFromIdCustomerHash));
        System.out.println("rightIdBanknoteFromIdCustomerRandom1: " + Arrays.toString(this.rightIdBanknoteFromIdCustomerRandom1));
        System.out.println("rightIdBanknoteFromIdCustomerRandom2: " + Arrays.toString(this.rightIdBanknoteFromIdCustomerRandom2));
        System.out.println("rightIdBanknoteFromIdCustomerHash: " + Arrays.toString(this.rightIdBanknoteFromIdCustomerHash));
    }

    private void showLeftIdBanknoteFromIdCustomer() {
        int i=0;
        for (byte[] b : this.leftIdBanknoteFromIdCustomerList) {
            i++;
            System.out.println(i+" leftIdBanknoteFromIdCustomer "+Arrays.toString(b));
        }
    }
}
