/**
 * Klasa odpowiedzialna za tworzenie banknotów przez Alice
 */
package pl.edu.amu.wmi.common.objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patryk
 */
public class BanknotesGenerator {
    
    private final List<Banknote> banknotesList = new ArrayList<>();
    private final byte[] customerId;

    public BanknotesGenerator(byte[] customerId) {
        this.customerId = customerId;
    }    
    public void banknotesGenerate(String amount){
        for (int i=0;i<99;i++){
            this.banknotesList.add(new Banknote(amount, this.customerId));           
        }
    }
    
    
    
}
