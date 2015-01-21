package pl.edu.amu.wmi.customer.services;


import com.google.common.base.Preconditions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.edu.amu.wmi.customer.BankPublicKey;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.security.PublicKey;

/**
 * Created by Tomasz on 2015-01-20.
 */
public class BankPublicKeyReceiverService implements MessageListener, ApplicationContextAware {

    ApplicationContext context;

    @Override
    public void onMessage(Message message) {
        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {

            if(objectMessage.getObject() instanceof PublicKey)
                System.out.println("Klient: otrzymałem klucz publiczny od banku");

            BankPublicKey bankPublicKey = (BankPublicKey) context.getBean("bankPublicKey");
            bankPublicKey.setBankPublicKey((PublicKey) objectMessage.getObject());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
