package pl.edu.amu.wmi.customer.services;

import com.google.common.base.Preconditions;
import java.security.interfaces.RSAPublicKey;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.SignedBanknote;
import pl.edu.amu.wmi.common.objects.UnblindingKeysResponse;
import pl.edu.amu.wmi.common.objects.UnblindingKeysRequest;
import pl.edu.amu.wmi.customer.BankPublicKey;

import javax.jms.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.edu.amu.wmi.common.Util.util;
import pl.edu.amu.wmi.common.objects.BanknotesGenerator;

/**
 * Created by Tomasz on 2015-01-15.
 */
public class CashReceptionService implements MessageListener, ApplicationContextAware {

    ApplicationContext context;

    public Destination getCashReceptionQueue() {
        return cashReceptionQueue;
    }

    public Destination cashGenerationQueue;

    public void setCashGenerationQueue(Destination cashGenerationQueue) {
        this.cashGenerationQueue = cashGenerationQueue;
    }

    public void setCashReceptionQueue(Destination cashReceptionQueue) {
        this.cashReceptionQueue = cashReceptionQueue;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    private JmsTemplate jmsTemplate;

    private Destination cashReceptionQueue;

    private BankPublicKey bankPublicKey;

    public void setBankPublicKey(BankPublicKey bankPublicKey) {
        this.bankPublicKey = bankPublicKey;
    }

    private BanknotesGenerator banknotesGenerator;
    
    @Override
    public void onMessage(final Message message) {
        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {
            if (objectMessage.getObject() instanceof UnblindingKeysRequest) {

                /**
                 * Klient otrzymuje żądanie przesłania do banku kluczy
                 * odtajniających dla 99 banknotów TODO: cała reszta
                 */
                UnblindingKeysRequest unblindingKeysRequest = (UnblindingKeysRequest) objectMessage.getObject();

                System.out.println("Klient: otrzymałem od banku żądanie przekazania kluczy.");

                jmsTemplate.send(objectMessage.getJMSReplyTo(), new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();
                        UnblindingKeysResponse unblindingKeysResponse 
                                = banknotesGenerator.getUnblindedBanknotes(unblindingKeysRequest.getNumber());

                        replyMessage.setObject(unblindingKeysResponse);
                        replyMessage.setJMSReplyTo(cashReceptionQueue);

                        System.out.println("Klient: wysyłam do banku zestaw kluczy.");

                        return replyMessage;
                    }
                });
            }

            if (objectMessage.getObject() instanceof SignedBanknote) {

                /**
                 * Klient otrzymuje podpisany banknot TODO: cała reszta
                 */
                SignedBanknote signedBanknote = (SignedBanknote) objectMessage.getObject();

                System.out.println("Klient: otrzymałem podpisany banknot.");

            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void generateCash(final String text) {
        jmsTemplate.send(cashGenerationQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                banknotesGenerator = new BanknotesGenerator(util.generateSecureRandom(16));
                banknotesGenerator.banknotesGenerate("1000");

                banknotesGenerator.blindBanknotesInBytes((RSAPublicKey)bankPublicKey.getBankPublicKey());
                
                ObjectMessage message = session.createObjectMessage();
                message.setObject(banknotesGenerator.getBanknotesBlindedList());
                message.setJMSReplyTo(cashReceptionQueue);

                System.out.println("Klient: wysyłam do banku żądanie wygenerowania banknotu.");

                return message;
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
