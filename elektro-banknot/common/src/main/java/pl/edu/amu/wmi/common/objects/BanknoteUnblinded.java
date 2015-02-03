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

    private final List<byte[]> leftIdBanknoteFromIdCustomerList;
    private final List<byte[]> rightIdBanknoteFromIdCustomerList;
    private List<byte[]> leftIdBanknoteFromIdCustomerRandom1List;
    private List<byte[]> leftIdBanknoteFromIdCustomerRandom2List;
    private List<byte[]> leftIdBanknoteFromIdCustomerHashList;

    private List<byte[]> rightIdBanknoteFromIdCustomerRandom1List;
    private List<byte[]> rightIdBanknoteFromIdCustomerRandom2List;
    private List<byte[]> rightIdBanknoteFromIdCustomerHashList;

    public BanknoteUnblinded(
            byte[] amount, 
            byte[] uniquenessString, 
            List<byte[]> leftIdBanknoteFromIdCustomerRandom1List, 
            List<byte[]> leftIdBanknoteFromIdCustomerRandom2List, 
            List<byte[]> leftIdBanknoteFromIdCustomerHashList, 
            List<byte[]> rightIdBanknoteFromIdCustomerRandom1List, 
            List<byte[]> rightIdBanknoteFromIdCustomerRandom2List, 
            List<byte[]> rightIdBanknoteFromIdCustomerHashList) {
        this.amount = amount;
        this.uniquenessString = uniquenessString;
        this.leftIdBanknoteFromIdCustomerRandom1List = leftIdBanknoteFromIdCustomerRandom1List;
        this.leftIdBanknoteFromIdCustomerRandom2List = leftIdBanknoteFromIdCustomerRandom2List;
        this.leftIdBanknoteFromIdCustomerHashList = leftIdBanknoteFromIdCustomerHashList;
        this.rightIdBanknoteFromIdCustomerRandom1List = rightIdBanknoteFromIdCustomerRandom1List;
        this.rightIdBanknoteFromIdCustomerRandom2List = rightIdBanknoteFromIdCustomerRandom2List;
        this.rightIdBanknoteFromIdCustomerHashList = rightIdBanknoteFromIdCustomerHashList;
        this.leftIdBanknoteFromIdCustomerList = null;
        this.rightIdBanknoteFromIdCustomerList = null;
    }

    public BanknoteUnblinded(
            byte[] amount, 
            byte[] uniquenessString, 
            List<byte[]> leftIdBanknoteFromIdCustomerList, 
            List<byte[]> rightIdBanknoteFromIdCustomerList, 
            List<byte[]> leftIdBanknoteFromIdCustomerRandom1List, 
            List<byte[]> leftIdBanknoteFromIdCustomerRandom2List, 
            List<byte[]> leftIdBanknoteFromIdCustomerHashList, 
            List<byte[]> rightIdBanknoteFromIdCustomerRandom1List, 
            List<byte[]> rightIdBanknoteFromIdCustomerRandom2List, 
            List<byte[]> rightIdBanknoteFromIdCustomerHashList) {
        this.amount = amount;
        this.uniquenessString = uniquenessString;
        this.leftIdBanknoteFromIdCustomerList = leftIdBanknoteFromIdCustomerList;
        this.rightIdBanknoteFromIdCustomerList = rightIdBanknoteFromIdCustomerList;
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

    public byte[] getUniquenessString() {
        return uniquenessString;
    }

    public List<byte[]> getLeftIdBanknoteFromIdCustomerList() {
        return leftIdBanknoteFromIdCustomerList;
    }

    public List<byte[]> getRightIdBanknoteFromIdCustomerList() {
        return rightIdBanknoteFromIdCustomerList;
    }

    public void setLeftIdBanknoteFromIdCustomerRandom2List(List<byte[]> leftIdBanknoteFromIdCustomerRandom2List) {
        this.leftIdBanknoteFromIdCustomerRandom2List = leftIdBanknoteFromIdCustomerRandom2List;
    }

    public void setRightIdBanknoteFromIdCustomerRandom2List(List<byte[]> rightIdBanknoteFromIdCustomerRandom2List) {
        this.rightIdBanknoteFromIdCustomerRandom2List = rightIdBanknoteFromIdCustomerRandom2List;
    }

}
