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
public class Banknote implements Serializable,BanknoteIface {

    private String amount;
    private byte[] uniquenessString;

    private final List<byte[]> leftIdBanknoteFromIdCustomerList = new ArrayList<>();
    private final List<byte[]> rightIdBanknoteFromIdCustomerList = new ArrayList<>();

    private final List<byte[]> leftIdBanknoteFromIdCustomerRandom1List = new ArrayList<>();
    private final List<byte[]> leftIdBanknoteFromIdCustomerRandom2List = new ArrayList<>();
    private final List<byte[]> leftIdBanknoteFromIdCustomerHashList = new ArrayList<>();

    private final List<byte[]> rightIdBanknoteFromIdCustomerRandom1List = new ArrayList<>();
    private final List<byte[]> rightIdBanknoteFromIdCustomerRandom2List = new ArrayList<>();
    private final List<byte[]> rightIdBanknoteFromIdCustomerHashList = new ArrayList<>();

    public Banknote(String amount, byte[] customerId) {
        this.amount = amount;
        this.generateUniquenessString();
        this.generateCustomerIdInBanknote(customerId);
        this.showAll();
    }

    public String getAmount() {
        return amount;
    }

    public List<byte[]> getLeftIdBanknoteFromIdCustomerHashList() {
        return leftIdBanknoteFromIdCustomerHashList;
    }

    public List<byte[]> getLeftIdBanknoteFromIdCustomerList() {
        return leftIdBanknoteFromIdCustomerList;
    }

    public List<byte[]> getLeftIdBanknoteFromIdCustomerRandom1List() {
        return leftIdBanknoteFromIdCustomerRandom1List;
    }

    public List<byte[]> getLeftIdBanknoteFromIdCustomerRandom2List() {
        return leftIdBanknoteFromIdCustomerRandom2List;
    }

    public List<byte[]> getRightIdBanknoteFromIdCustomerHashList() {
        return rightIdBanknoteFromIdCustomerHashList;
    }

    public List<byte[]> getRightIdBanknoteFromIdCustomerList() {
        return rightIdBanknoteFromIdCustomerList;
    }

    public List<byte[]> getRightIdBanknoteFromIdCustomerRandom1List() {
        return rightIdBanknoteFromIdCustomerRandom1List;
    }

    public List<byte[]> getRightIdBanknoteFromIdCustomerRandom2List() {
        return rightIdBanknoteFromIdCustomerRandom2List;
    }
    
    public void setAmount(String value) {
        this.amount = value;
    }

    public byte[] getUniquenessString() {
        return uniquenessString;
    }

    //Generuje identyfikator banknotu
    private void generateUniquenessString() {
        this.uniquenessString = util.generateSecureRandom(lengthUniquenessString);
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
                this.generateHashCommitmentCustomerIdInBanknote(secretId.getRandom1(), secretId.getResult());
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchProviderException ex) {
                Logger.getLogger(Banknote.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void generateHashCommitmentCustomerIdInBanknote(byte[] left, byte[] right) {
        //Generowanie zobowiazania do lewej strony
        HashCommitmentScheme leftIdBanknoteFromIdCustomerCommitmentScheme
                = new HashCommitmentScheme(left);
        this.leftIdBanknoteFromIdCustomerRandom1List.add(
                leftIdBanknoteFromIdCustomerCommitmentScheme.getRandom1());
        this.leftIdBanknoteFromIdCustomerRandom2List.add(
                leftIdBanknoteFromIdCustomerCommitmentScheme.getRandom2());
        this.leftIdBanknoteFromIdCustomerHashList.add(
                leftIdBanknoteFromIdCustomerCommitmentScheme.getHash());
        //Generowanie zoobowiazania do prawej strony
        HashCommitmentScheme rightIdBanknoteFromIdCustomerCommitmentScheme
                = new HashCommitmentScheme(right);
        this.rightIdBanknoteFromIdCustomerRandom1List.add(
                rightIdBanknoteFromIdCustomerCommitmentScheme.getRandom1());
        this.rightIdBanknoteFromIdCustomerRandom2List.add(
                rightIdBanknoteFromIdCustomerCommitmentScheme.getRandom2());
        this.rightIdBanknoteFromIdCustomerHashList.add(
                rightIdBanknoteFromIdCustomerCommitmentScheme.getHash());
    }

    private void showAll() {
        this.showLeftIdBanknoteFromIdCustomer();
        this.showRightIdBanknoteFromIdCustomer();
        this.showLeftIdBanknoteFromIdCustomerRandom1();
        this.showLeftIdBanknoteFromIdCustomerRandom2();
        this.showLeftIdBanknoteFromIdCustomerHash();
        this.showRightIdBanknoteFromIdCustomerRandom1();
        this.showRightIdBanknoteFromIdCustomerRandom2();
        this.showRightIdBanknoteFromIdCustomerHash();
    }

    private void showLeftIdBanknoteFromIdCustomer() {
        int i = 0;
        for (byte[] b : this.leftIdBanknoteFromIdCustomerList) {
            i++;
            System.out.println(i + " leftIdBanknoteFromIdCustomer " + Arrays.toString(b));
        }
    }

    private void showRightIdBanknoteFromIdCustomer() {
        int i = 0;
        for (byte[] b : this.rightIdBanknoteFromIdCustomerList) {
            i++;
            System.out.println(i + " rightIdBanknoteFromIdCustomer " + Arrays.toString(b));
        }
    }

    private void showLeftIdBanknoteFromIdCustomerRandom1() {
        int i = 0;
        for (byte[] b : this.leftIdBanknoteFromIdCustomerRandom1List) {
            i++;
            System.out.println(i + " LeftIdBanknoteFromIdCustomerRandom1 " + Arrays.toString(b));
        }
    }

    private void showLeftIdBanknoteFromIdCustomerRandom2() {
        int i = 0;
        for (byte[] b : this.leftIdBanknoteFromIdCustomerRandom2List) {
            i++;
            System.out.println(i + " leftIdBanknoteFromIdCustomerRandom2 " + Arrays.toString(b));
        }
    }

    private void showLeftIdBanknoteFromIdCustomerHash() {
        int i = 0;
        for (byte[] b : this.leftIdBanknoteFromIdCustomerHashList) {
            i++;
            System.out.println(i + " leftIdBanknoteFromIdCustomerHash " + Arrays.toString(b));
        }

    }

    private void showRightIdBanknoteFromIdCustomerRandom1() {
        int i = 0;
        for (byte[] b : this.rightIdBanknoteFromIdCustomerRandom1List) {
            i++;
            System.out.println(i + " RightIdBanknoteFromIdCustomerRandom1 " + Arrays.toString(b));
        }
    }

    private void showRightIdBanknoteFromIdCustomerRandom2() {
        int i = 0;
        for (byte[] b : this.rightIdBanknoteFromIdCustomerRandom2List) {
            i++;
            System.out.println(i + " RightIdBanknoteFromIdCustomerRandom2 " + Arrays.toString(b));
        }
    }

    private void showRightIdBanknoteFromIdCustomerHash() {
        int i = 0;
        for (byte[] b : this.rightIdBanknoteFromIdCustomerHashList) {
            i++;
            System.out.println(i + " RightIdBanknoteFromIdCustomerHash " + Arrays.toString(b));
        }
    }

}
