/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.amu.wmi.shop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.amu.wmi.shop.services.BankPublicKeyReceiverService;

import java.security.PublicKey;

/**
 *
 * @author Patryk
 */
public class Shop {
    public static void main(String[] args) throws InterruptedException {

        ApplicationContext context = new ClassPathXmlApplicationContext("Context.xml");
        
        Thread.sleep(1000);

        PublicKey publicKey = ((BankPublicKeyReceiverService) context.getBean("bankPublicKeyReceiverService")).getBankPublicKey();

    }
}
