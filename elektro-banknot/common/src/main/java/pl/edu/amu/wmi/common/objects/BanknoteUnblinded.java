/*
 * Banknot, odblindowany do sprawdzenia poprawności w banku
 */
package pl.edu.amu.wmi.common.objects;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Patryk
 */
public class BanknoteUnblinded implements Serializable {

    private byte[] amount;
    private byte[] uniquenessString;

    private List<byte[]> leftIdBanknoteFromIdCustomerRandom1List;
    private List<byte[]> leftIdBanknoteFromIdCustomerRandom2List;
    private List<byte[]> leftIdBanknoteFromIdCustomerHashList;

    private List<byte[]> rightIdBanknoteFromIdCustomerRandom1List;
    private List<byte[]> rightIdBanknoteFromIdCustomerRandom2List;
    private List<byte[]> rightIdBanknoteFromIdCustomerHashList;

    public BanknoteUnblinded(byte[] amount, byte[] uniquenessString, List<byte[]> leftIdBanknoteFromIdCustomerRandom1List, List<byte[]> leftIdBanknoteFromIdCustomerRandom2List, List<byte[]> leftIdBanknoteFromIdCustomerHashList, List<byte[]> rightIdBanknoteFromIdCustomerRandom1List, List<byte[]> rightIdBanknoteFromIdCustomerRandom2List, List<byte[]> rightIdBanknoteFromIdCustomerHashList) {
        this.amount = amount;
        this.uniquenessString = uniquenessString;
        this.leftIdBanknoteFromIdCustomerRandom1List = leftIdBanknoteFromIdCustomerRandom1List;
        this.leftIdBanknoteFromIdCustomerRandom2List = leftIdBanknoteFromIdCustomerRandom2List;
        this.leftIdBanknoteFromIdCustomerHashList = leftIdBanknoteFromIdCustomerHashList;
        this.rightIdBanknoteFromIdCustomerRandom1List = rightIdBanknoteFromIdCustomerRandom1List;
        this.rightIdBanknoteFromIdCustomerRandom2List = rightIdBanknoteFromIdCustomerRandom2List;
        this.rightIdBanknoteFromIdCustomerHashList = rightIdBanknoteFromIdCustomerHashList;
    }

    public byte[] getAmount() {
        return amount;
    }

    public List<byte[]> getLeftIdBanknoteFromIdCustomerHashList() {
        return leftIdBanknoteFromIdCustomerHashList;
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

    public List<byte[]> getRightIdBanknoteFromIdCustomerRandom1List() {
        return rightIdBanknoteFromIdCustomerRandom1List;
    }

    public List<byte[]> getRightIdBanknoteFromIdCustomerRandom2List() {
        return rightIdBanknoteFromIdCustomerRandom2List;
    }
    
}
