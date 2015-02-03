/*
 * Banknot zaślepiony przy użyciu RSABlind
 */
package pl.edu.amu.wmi.common.objects;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Patryk
 */
public class BanknoteBlinded implements Serializable{

    private byte[] amount;
    private byte[] uniquenessString;

    private List<byte[]> leftIdBanknoteFromIdCustomerRandom1List;
    private List<byte[]>leftIdBanknoteFromIdCustomerHashList;

    private List<byte[]> rightIdBanknoteFromIdCustomerRandom1List;
    private List<byte[]> rightIdBanknoteFromIdCustomerHashList;

    public BanknoteBlinded(byte[] amount, byte[] uniquenessString, List<byte[]> leftIdBanknoteFromIdCustomerRandom1List, List<byte[]> leftIdBanknoteFromIdCustomerHashList, List<byte[]> rightIdBanknoteFromIdCustomerRandom1List, List<byte[]> rightIdBanknoteFromIdCustomerHashList) {
        this.amount = amount;
        this.uniquenessString = uniquenessString;
        this.leftIdBanknoteFromIdCustomerRandom1List = leftIdBanknoteFromIdCustomerRandom1List;
        this.leftIdBanknoteFromIdCustomerHashList = leftIdBanknoteFromIdCustomerHashList;
        this.rightIdBanknoteFromIdCustomerRandom1List = rightIdBanknoteFromIdCustomerRandom1List;
        this.rightIdBanknoteFromIdCustomerHashList = rightIdBanknoteFromIdCustomerHashList;
//        this.show();
    }

    private void show(){
        System.out.println("Amount "+this.amount.length);
        System.out.println("ID "+this.uniquenessString.length);
        System.out.println("LeftID "+this.leftIdBanknoteFromIdCustomerRandom1List.get(0).length);
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

    public List<byte[]> getRightIdBanknoteFromIdCustomerHashList() {
        return rightIdBanknoteFromIdCustomerHashList;
    }

    public List<byte[]> getRightIdBanknoteFromIdCustomerRandom1List() {
        return rightIdBanknoteFromIdCustomerRandom1List;
    }

    public byte[] getUniquenessString() {
        return uniquenessString;
    }
    

}
