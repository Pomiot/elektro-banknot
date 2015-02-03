package pl.edu.amu.wmi.bank.billing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomasz on 2015-01-21.
 */
public class Accounts {

    private double customerBalance = 10000;
    private double shopBalance = 10000;

    public static List<byte[]> uniquenessStrings = new ArrayList<>();

    public void setCustomerBalance(double customerBalance) {
        this.customerBalance = customerBalance;
    }

    public void setShopBalance(double shopBalance) {
        this.shopBalance = shopBalance;
    }

    public List<byte[]> getUniquenessStrings() {
        return uniquenessStrings;
    }

    public void setUniquenessStrings(List<byte[]> uniquenessStrings) {
        this.uniquenessStrings = uniquenessStrings;
    }

    public Double getCustomerBalance(){
        return this.customerBalance;
    }

    public Double getShopBalance(){
        return this.shopBalance;
    }

    public void addToCustomerAccount(double amount){
        this.customerBalance += amount;
    }

    public void addToShopBalance(double amount){
        this.shopBalance += amount;
    }

    public void substractFromCustomerBalance(double amount){
        this.customerBalance -= amount;
    }

    public void substractFromShopBalance(double amount){
        this.shopBalance -= amount;
    }

    public String toString(){
        return "Konto klienta: "+this.customerBalance+"\nKonto sklepu: "+this.shopBalance;
    }
}
