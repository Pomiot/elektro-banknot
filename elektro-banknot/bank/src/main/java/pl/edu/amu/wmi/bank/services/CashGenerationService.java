package pl.edu.amu.wmi.bank.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.bank.Keys;
import pl.edu.amu.wmi.common.objects.BanknotesToGeneration;
import pl.edu.amu.wmi.common.objects.SignedBanknote;
import pl.edu.amu.wmi.common.objects.UnblindingKeysRequest;
import pl.edu.amu.wmi.common.objects.UnblindingKeysResponse;

import javax.jms.*;
import java.security.KeyPair;

/**
 * Created by Tomasz on 2015-01-08.
 *
 */
public class CashGenerationService implements MessageListener, ApplicationContextAware {

    private ApplicationContext context;

    private BanknotesToGeneration savedBanknotesToGeneration;

    private UnblindingKeysResponse savedUnblindingKeysResponse;

    private UnblindingKeysRequest savedUnblindingKeysRequest;

    private JmsTemplate jmsTemplate;

    private Destination cashGenerationQueue;

    public void setCashGenerationQueue(Destination cashGenerationQueue) {
        this.cashGenerationQueue = cashGenerationQueue;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void onMessage(Message message) {
        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {

            /**
             * Tutaj będzie odbywać się magia jak bank dostanie listę
             * zaciemnionych banknotów.
             *
             * TODO: bank losuje 99 spośród 100 banknotów i wysyła
             * UnblindingKeyRequest do customera
             */
            if (objectMessage.getObject() instanceof BanknotesToGeneration) {

                this.savedBanknotesToGeneration = (BanknotesToGeneration) objectMessage.getObject();
                System.out.println("Bank: otrzymałem od klienta żądanie wystawienia banknotu.");

                Destination replyTo = objectMessage.getJMSReplyTo();

                jmsTemplate.send(replyTo, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();

                        savedUnblindingKeysRequest
                                = ProcessBanknotesToGeneration.generateUnblindingKeysRequest(savedBanknotesToGeneration);

                        replyMessage.setObject(savedUnblindingKeysRequest);
                        replyMessage.setJMSReplyTo(cashGenerationQueue);

                        System.out.println("Bank: wysyłam do klienta żądanie przekazania kluczy.");

                        return replyMessage;
                    }
                });
            }

            if (objectMessage.getObject() instanceof UnblindingKeysResponse) {

                /**
                 * Tutaj będzie odbywać się magia jak już bank dostanie listę
                 * kluczy do banknotów.
                 *
                 * TODO: bank sprawdza 99 wcześniej wylosowanych banknotów i
                 * jeżeli wszystko jest OK TODO: podpisuje i wysyła
                 * SignedBanknote do customera
                 *
                 */
                savedUnblindingKeysResponse = (UnblindingKeysResponse) objectMessage.getObject();

                System.out.println("Bank: otrzymałem od klienta zestaw kluczy.");

                Destination replyTo = objectMessage.getJMSReplyTo();

                jmsTemplate.send(replyTo, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();

                        KeyPair kp = ((Keys) context.getBean("keys")).getKeyPair();
                        ProcessUnblindingKeysResponse pukr = new ProcessUnblindingKeysResponse(
                                savedUnblindingKeysResponse, savedBanknotesToGeneration, kp, savedUnblindingKeysRequest.getNumber());
                        SignedBanknote signedBanknote
                                = pukr.generateSignedBanknote();

                        replyMessage.setObject(signedBanknote);

                        System.out.println("Bank: wysyłam do klienta podpisany banknot.");

                        return replyMessage;
                    }
                });

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
