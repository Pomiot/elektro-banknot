package pl.edu.amu.wmi.customer.services;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.BanknotesToGeneration;
import pl.edu.amu.wmi.customer.BankPublicKey;

import javax.jms.*;

/**
 * Created by Tomasz on 2015-01-08.
 */
public class CustomerCashGenerationService {

    private JmsTemplate jmsTemplate;

    private Destination cashGenerationQueue;
    private Destination cashReceptionQueue;

    private BankPublicKey bankPublicKey;

    public void setBankPublicKey(BankPublicKey bankPublicKey) {
        this.bankPublicKey = bankPublicKey;
    }

    public void setCashReceptionQueue(Destination cashReceptionQueue) {
        this.cashReceptionQueue = cashReceptionQueue;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setCashGenerationQueue(Destination offerTopic) {
        this.cashGenerationQueue = offerTopic;
    }

    public void generateCash(final String text) {
        jmsTemplate.send(cashGenerationQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                BanknotesToGeneration banknotesToGeneration = new BanknotesToGeneration();

                ObjectMessage message = session.createObjectMessage();
                message.setObject(banknotesToGeneration);
                message.setJMSReplyTo(cashReceptionQueue);

                System.out.println("Klient: wysyłam do banku żądanie wygenerowania banknotu.");

                return message;
            }
        });
    }
}
