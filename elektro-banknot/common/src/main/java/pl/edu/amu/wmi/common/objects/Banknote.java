package pl.edu.amu.wmi.common.objects;
/**
Klasa stanawiąca banknot
amount - kwota banknotu
uniquenessString - identyfikator banknotu
* identyfikator jest byte[] wynika to  tego ze SecureRandom działa na tym
**/
import java.io.Serializable;
import pl.edu.amu.wmi.common.Util.util;
/**
 * Created by Tomasz on 2015-01-15.
 * Modification by Patryk on 2015-01-19
  *
 */
public class Banknote implements Serializable {
    private String amount;
    private byte[] uniquenessString;

    public Banknote(String amount) {
        this.amount = amount;
        this.generateUniquenessString();
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
    private void generateUniquenessString(){
        this.uniquenessString = util.generateSecureRandom(16);
    }
}
