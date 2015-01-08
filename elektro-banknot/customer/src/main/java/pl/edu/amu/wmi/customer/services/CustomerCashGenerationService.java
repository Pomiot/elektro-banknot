package pl.edu.amu.wmi.customer.services;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

import javax.jms.Destination;

/**
 * Created by Tomasz on 2015-01-08.
 */
public class CustomerCashGenerationService {

    private JmsTemplate jmsTemplate;

    private Destination cashGenerationQueue;


    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setCashGenerationQueue(Destination offerTopic) {
        this.cashGenerationQueue = offerTopic;
    }

    public void generateCash(final String text){
        jmsTemplate.send(cashGenerationQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("test", "Jazda jazda biala gwiazda: "+text);

                return message;
            }
        });
    }
}
