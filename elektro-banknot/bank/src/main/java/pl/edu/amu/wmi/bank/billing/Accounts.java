package pl.edu.amu.wmi.bank.billing;

/**
 * Created by Tomasz on 2015-01-21.
 */
public class Accounts {

    private double customerBalance = 10000;
    private double shopBalance = 10000;

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
