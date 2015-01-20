package pl.edu.amu.wmi.shop.services;


import com.google.common.base.Preconditions;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.security.PublicKey;

/**
 * Created by Tomasz on 2015-01-20.
 */
public class BankPublicKeyReceiverService implements MessageListener {

    PublicKey bankPublicKey;

    public void setBankPublicKey(PublicKey bankPublicKey) {
        this.bankPublicKey = bankPublicKey;
    }

    @Override
    public void onMessage(Message message) {
        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {

            if(objectMessage.getObject() instanceof PublicKey)
            System.out.println("Sklep: otrzymałem klucz publiczny od banku");

            setBankPublicKey((PublicKey) objectMessage.getObject());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
