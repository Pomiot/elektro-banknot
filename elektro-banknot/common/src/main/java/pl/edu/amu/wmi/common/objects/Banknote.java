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

    public Banknote(String amount, byte[] customerId) {
        this.amount = amount;
        this.generateUniquenessString();
        System.out.println(">---Customer ID: " + Arrays.toString(customerId));
        this.generateCustomerIdInBanknote(customerId);
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
            System.out.println(">--Customer ID przed blind: " + Arrays.toString(customerId));
            //Test czy faktycznie dziala
            int i = 0;
            byte[] result = new byte[customerId.length];
            for (byte b : this.leftIdBanknoteFromIdCustomer) {
                result[i] = (byte) (b ^ this.rightIdBanknoteFromIdCustomer[i]);
                i++;
            }
            System.out.println(">--Customer ID po blind: "+Arrays.toString(result));

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(Banknote.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
