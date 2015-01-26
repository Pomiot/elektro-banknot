package pl.edu.amu.wmi.customer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.amu.wmi.customer.services.BankPublicKeyReceiverService;
import pl.edu.amu.wmi.customer.services.CashReceptionService;
import pl.edu.amu.wmi.customer.services.PurchaseFromShopService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

public class Customer {
    public static void main(String[] args) throws InterruptedException, IOException, NoSuchAlgorithmException {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ApplicationContext context = new ClassPathXmlApplicationContext("Context.xml");
        CashReceptionService cgs = (CashReceptionService) context.getBean("cashReceptionService");
        PurchaseFromShopService pfs = (PurchaseFromShopService) context.getBean("purchaseFromShopService");
        BankPublicKeyReceiverService keyReceiverService = (BankPublicKeyReceiverService) context.getBean("bankPublicKeyReceiverService");


        System.out.println("*** Aplikacja kliencka uruchomiona ***");
        System.out.println("*** Poczekaj na otrzymanie klucza i wciśnij Enter ***");

        reader.readLine();

        cgs.generateCash("DUUUUUUUUPA");

        pfs.makePurchase();
        
        //System.out.println("Run customer application");
        //UI ui = new UI();
        //ui.start();
    }
    
}
