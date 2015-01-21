package pl.edu.amu.wmi.bank.services;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.bank.Keys;

import javax.jms.*;

/**
 * Created by Tomasz on 2015-01-20.
 */
public class SendPublicKeyService {

    JmsTemplate jmsTemplate;

    Destination bankPublicKeyQueueForCustomer;
    Destination bankPublicKeyQueueForShop;

    public void setKeys(Keys keys) {
        this.keys = keys;
    }

    Keys keys;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setBankPublicKeyQueueForCustomer(Destination bankPublicKeyQueueForCustomer) {
        this.bankPublicKeyQueueForCustomer = bankPublicKeyQueueForCustomer;
    }

    public void setBankPublicKeyQueueForShop(Destination bankPublicKeyQueueForShop) {
        this.bankPublicKeyQueueForShop = bankPublicKeyQueueForShop;
    }

    public void sendPublicKey(){

        jmsTemplate.send(bankPublicKeyQueueForShop, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                ObjectMessage message = session.createObjectMessage();
                message.setObject(keys.getPublicKey());

                System.out.println("Bank: wysyłam swój publiczny klucz do sklepu");

                return message;
            }
        });

        jmsTemplate.send(bankPublicKeyQueueForCustomer, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                ObjectMessage message = session.createObjectMessage();
                message.setObject(keys.getPublicKey());

                System.out.println("Bank: wysyłam swój publiczny klucz do klienta");

                return message;
            }
        });
    }
}
