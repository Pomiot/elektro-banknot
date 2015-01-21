package pl.edu.amu.wmi.common.objects;

/**
 * zestaw zaciemnionych banknotów, które ma otrzymać bank.
 *
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomasz on 2015-01-18.
 */
public class BanknotesToGeneration implements Serializable {

    private int lengthAmount;
    private int lengthUniquenessString;
    private int lengthLeftIdBanknoteFromIdCustomer;
    private int lengthRightIdBanknoteFromIdCustomer;

    private int lengthLeftIdBanknoteFromIdCustomerRandom1;
    private int lengthLeftIdBanknoteFromIdCustomerRandom2;
    private int lengthLeftIdBanknoteFromIdCustomerHash;

    private int lengthRightIdBanknoteFromIdCustomerRandom1;
    private int lengthRightIdBanknoteFromIdCustomerRandom2;
    private int lengthRightIdBanknoteFromIdCustomerHash;

    private final byte[] banknotesBytesArray;

    public BanknotesToGeneration(int lengthAmount,
            int lengthUniquenessString,
            int lengthLeftIdBanknoteFromIdCustomer,
            int lengthRightIdBanknoteFromIdCustomer,
            int lengthLeftIdBanknoteFromIdCustomerRandom1,
            int lengthLeftIdBanknoteFromIdCustomerRandom2,
            int lengthLeftIdBanknoteFromIdCustomerHash,
            int lengthRightIdBanknoteFromIdCustomerRandom1,
            int lengthRightIdBanknoteFromIdCustomerRandom2,
            int lengthRightIdBanknoteFromIdCustomerHash,
            byte[] banknotesBytesArray) {
        this.lengthAmount = lengthAmount;
        this.lengthUniquenessString = lengthUniquenessString;
        this.lengthLeftIdBanknoteFromIdCustomer = lengthLeftIdBanknoteFromIdCustomer;
        this.lengthRightIdBanknoteFromIdCustomer = lengthRightIdBanknoteFromIdCustomer;
        this.lengthLeftIdBanknoteFromIdCustomerRandom1 = lengthLeftIdBanknoteFromIdCustomerRandom1;
        this.lengthLeftIdBanknoteFromIdCustomerRandom2 = lengthLeftIdBanknoteFromIdCustomerRandom2;
        this.lengthLeftIdBanknoteFromIdCustomerHash = lengthLeftIdBanknoteFromIdCustomerHash;
        this.lengthRightIdBanknoteFromIdCustomerRandom1 = lengthRightIdBanknoteFromIdCustomerRandom1;
        this.lengthRightIdBanknoteFromIdCustomerRandom2 = lengthRightIdBanknoteFromIdCustomerRandom2;
        this.lengthRightIdBanknoteFromIdCustomerHash = lengthRightIdBanknoteFromIdCustomerHash;
        this.banknotesBytesArray = banknotesBytesArray;
    }

    public int getLengthAmount() {
        return lengthAmount;
    }

    public int getLengthLeftIdBanknoteFromIdCustomer() {
        return lengthLeftIdBanknoteFromIdCustomer;
    }

    public int getLengthLeftIdBanknoteFromIdCustomerHash() {
        return lengthLeftIdBanknoteFromIdCustomerHash;
    }

    public int getLengthLeftIdBanknoteFromIdCustomerRandom1() {
        return lengthLeftIdBanknoteFromIdCustomerRandom1;
    }

    public int getLengthLeftIdBanknoteFromIdCustomerRandom2() {
        return lengthLeftIdBanknoteFromIdCustomerRandom2;
    }

    public int getLengthRightIdBanknoteFromIdCustomer() {
        return lengthRightIdBanknoteFromIdCustomer;
    }

    public int getLengthRightIdBanknoteFromIdCustomerHash() {
        return lengthRightIdBanknoteFromIdCustomerHash;
    }

    public int getLengthRightIdBanknoteFromIdCustomerRandom1() {
        return lengthRightIdBanknoteFromIdCustomerRandom1;
    }

    public int getLengthRightIdBanknoteFromIdCustomerRandom2() {
        return lengthRightIdBanknoteFromIdCustomerRandom2;
    }

    public int getLengthUniquenessString() {
        return lengthUniquenessString;
    }

    public byte[] getBanknotesBytesArray() {
        return banknotesBytesArray;
    }

}
