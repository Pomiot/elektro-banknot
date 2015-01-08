package pl.edu.amu.wmi.bank.services;

import com.google.common.base.Preconditions;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.lang.IllegalStateException;

/**
 * Created by Tomasz on 2015-01-08.
 */
public class CashGenerationService implements MessageListener {


    private JmsTemplate jmsTemplate;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void onMessage(Message message) {
        Preconditions.checkArgument(message instanceof MapMessage);

        MapMessage mapMessage = (MapMessage) message;
        try {
            System.out.println(mapMessage.getString("test"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
