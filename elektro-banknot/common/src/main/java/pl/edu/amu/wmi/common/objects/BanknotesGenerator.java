/**
 * Klasa odpowiedzialna za tworzenie banknotów przez Alice
 */
package pl.edu.amu.wmi.common.objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public byte[] banknotesInBytes() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        for (Banknote banknote: this.banknotesList){
            bytes.write(banknote.getAmount().getBytes());
            bytes.write(banknote.getUniquenessString());
            for (byte[] leftID: banknote.getLeftIdBanknoteFromIdCustomerList()){
                bytes.write(leftID);
            }
            for (byte[] leftID: banknote.getLeftIdBanknoteFromIdCustomerRandom1List()){
                bytes.write(leftID);
            }
            for (byte[] leftID: banknote.getLeftIdBanknoteFromIdCustomerRandom2List()){
                bytes.write(leftID);
            }
            for (byte[] leftID: banknote.getLeftIdBanknoteFromIdCustomerHashList()){
                bytes.write(leftID);
            }
            for (byte[] rightID: banknote.getRightIdBanknoteFromIdCustomerList()){
                bytes.write(rightID);
            }
            for (byte[] rightID: banknote.getRightIdBanknoteFromIdCustomerRandom1List()){
                bytes.write(rightID);
            }
            for (byte[] rightID: banknote.getRightIdBanknoteFromIdCustomerRandom2List()){
                bytes.write(rightID);
            }
            for (byte[] rightID: banknote.getRightIdBanknoteFromIdCustomerHashList()){
                bytes.write(rightID);
            }
        }            
        return bytes.toByteArray();
    }
    
    
    
}
