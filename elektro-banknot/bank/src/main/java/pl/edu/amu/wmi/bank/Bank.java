/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.amu.wmi.bank;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.amu.wmi.bank.billing.Accounts;
import pl.edu.amu.wmi.bank.services.SendPublicKeyToShopService;
import sun.security.rsa.RSAKeyPairGenerator;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;


/**
 * @author Patryk
 */
public class Bank {
    public static void main(String[] args) throws InterruptedException {


        ApplicationContext context = new ClassPathXmlApplicationContext("Context.xml");

        SendPublicKeyToShopService keySender = (SendPublicKeyToShopService) context.getBean("sendPublicKeyToShopService");

        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        rsaKeyPairGenerator.initialize(512,new SecureRandom());
        KeyPair keyPair = rsaKeyPairGenerator.generateKeyPair();

        final PublicKey publicKey = keyPair.getPublic();
        final PrivateKey privateKey = keyPair.getPrivate();

        keySender.sendPublicKey(publicKey);

        Accounts accounts = (Accounts) context.getBean("accounts");

        System.out.println(accounts);

    }
}
