package pl.edu.amu.wmi.shop.services;

import com.google.common.base.Preconditions;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;

/**
 * Created by Tomasz on 2015-01-18.
 */
public class PurchaseService implements MessageListener {

    JmsTemplate jmsTemplate;

    Destination purchaseQueue;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setPurchaseQueue(Destination purchaseQueue) {
        this.purchaseQueue = purchaseQueue;
    }

    @Override
    public void onMessage(Message message) {

        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {
            System.out.println("Sklep: otrzymałem wiadomość. Klient chce zakupić " + objectMessage.getStringProperty("item"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
