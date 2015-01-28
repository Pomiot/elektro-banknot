package pl.edu.amu.wmi.common.objects;

/**
 * zestaw zaciemnionych banknotów, które ma otrzymać bank.
 *
 */
import java.io.Serializable;
import java.util.List;

/**
 * Created by Tomasz on 2015-01-18.
 */
public class BanknotesToGeneration implements Serializable {

    private List<BanknoteBlinded> blindedBanknotesList;

    public BanknotesToGeneration(List<BanknoteBlinded> blindedBanknotesList) {
        this.blindedBanknotesList = blindedBanknotesList;
    }

    public List<BanknoteBlinded> getBlindedBanknotesList() {
        return blindedBanknotesList;
    }
    
    

}
