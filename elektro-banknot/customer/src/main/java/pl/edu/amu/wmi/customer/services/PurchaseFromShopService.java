package pl.edu.amu.wmi.customer.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.Banknote;
import pl.edu.amu.wmi.customer.BankPublicKey;
import javax.jms.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.edu.amu.wmi.common.Util.util;
import pl.edu.amu.wmi.common.cryptography.RSA;
import pl.edu.amu.wmi.common.objects.BanknotesGenerator;

/**
 * Created by Tomasz on 2015-01-18.
 *
 */
public class PurchaseFromShopService implements ApplicationContextAware {

    JmsTemplate jmsTemplate;

    Destination purchaseQueue;

    Destination identificationSendingQueue;

    ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext ac) {
        this.context = ac;
    }

    public void setIdentificationSendingQueue(Destination identificationSendingQueue) {
        this.identificationSendingQueue = identificationSendingQueue;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setPurchaseQueue(Destination purchaseQueue) {
        this.purchaseQueue = purchaseQueue;
    }

    public void makePurchase() {
        jmsTemplate.send(purchaseQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                ObjectMessage message = session.createObjectMessage();

                message.setStringProperty("item", "fortepian");
                //Tymczasowa modyfikacja do testów

                Banknote banknot = new Banknote("1000", util.generateSecureRandom(16));
//                BanknotesGenerator generator = new BanknotesGenerator(util.generateSecureRandom(16));
//                generator.banknotesGenerate("1000");
//                try {
//                    RSA rsa = new RSA();
//                    rsa.GeneratePairKey();
//                    byte[] encrypted = rsa.EncryptTransfer(generator.banknotesInBytes());
//                    System.out.println("encrypted" + encrypted.length);
//                    byte[] decrypted = rsa.DecryptTransfer(encrypted);
//                    System.out.println("decrypted" + decrypted.length);
//                    byte[] dupa = generator.banknoteInBytes();
//                    for (int i =0;i<generator.banknoteInBytes().length;i++){
//                        if (decrypted[i] != dupa[i]){
//                            System.out.println("Zle liczy");
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                message.setObject(banknot);
                message.setJMSReplyTo(identificationSendingQueue);

                System.out.println("Klient: wysyłam do sklepu polecenie dokonania zakupu oraz banknot");

                return message;
            }
        });
    }
}
