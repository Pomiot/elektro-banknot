package pl.edu.amu.wmi.customer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.amu.wmi.customer.services.CustomerCashGenerationService;
import pl.edu.amu.wmi.customer.services.PurchaseFromShopService;

public class Customer {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("Context.xml");
        CustomerCashGenerationService cgs = (CustomerCashGenerationService) context.getBean("customerCashGenerationService");
        PurchaseFromShopService pfs = (PurchaseFromShopService) context.getBean("purchaseFromShopService");

        cgs.generateCash("DUUUUUUUUPA");

        pfs.makePurchase();
        
        //System.out.println("Run customer application");
        //UI ui = new UI();
        //ui.start();
    }
    
}
