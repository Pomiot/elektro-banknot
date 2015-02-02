package pl.edu.amu.wmi.customer.services;

import java.util.Arrays;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.Banknote;
import javax.jms.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.edu.amu.wmi.common.Util.util;
import pl.edu.amu.wmi.common.objects.BanknotesGenerator;

/**
 * Created by Tomasz on 2015-01-18.
 *
 */
public class PurchaseFromShopService implements ApplicationContextAware {    
    
    
    private JmsTemplate jmsTemplate;

    private Destination purchaseQueue;

    private Destination identificationSendingQueue;

    private ApplicationContext context;
    
    private BanknotesGenerator banknotesGenerator;

    public void setBanknotesGenerator(BanknotesGenerator banknotesGenerator) {
        this.banknotesGenerator = banknotesGenerator;
    }    

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
                message.setObject(banknotesGenerator.getBanknotePairToShop());

                message.setJMSReplyTo(identificationSendingQueue);

                System.out.println(
                        "Klient: wysyłam do sklepu polecenie dokonania zakupu oraz banknot");

                return message;
            }
        }
        );
    }
}
