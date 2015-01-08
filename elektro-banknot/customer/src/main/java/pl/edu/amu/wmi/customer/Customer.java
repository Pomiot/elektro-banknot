package pl.edu.amu.wmi.customer;

import pl.edu.amu.wmi.customer.UI.UI;

public class Customer {
    public static void main(String[] args) {
         ApplicationContext context = new ClassPathXmlApplicationContext("Context.xml");
        
        System.out.println("Run customer application");
        UI ui = new UI();
        ui.start();
    }
    
}
