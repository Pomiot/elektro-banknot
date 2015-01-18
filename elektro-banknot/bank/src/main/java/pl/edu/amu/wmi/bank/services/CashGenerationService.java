package pl.edu.amu.wmi.bank.services;

import com.google.common.base.Preconditions;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.BanknotesToGeneration;
import pl.edu.amu.wmi.common.objects.SignedBanknote;
import pl.edu.amu.wmi.common.objects.UnblindingKeysArray;
import pl.edu.amu.wmi.common.objects.UnblindingKeysRequest;

import javax.jms.*;

/**
 * Created by Tomasz on 2015-01-08.
 **/
public class CashGenerationService implements MessageListener {

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

        ObjectMessage objectMessage= (ObjectMessage) message;

        try {

            /**
             * Tutaj będzie odbywać się magia jak bank dostanie listę zaciemnionych banknotów.
             *
             * TODO: bank losuje 99 spośród 100 banknotów i wysyła UnblindingKeyRequest do customera
             */

            if(objectMessage.getObject() instanceof BanknotesToGeneration){

                BanknotesToGeneration banknotesToGeneration = (BanknotesToGeneration) objectMessage.getObject();

                System.out.println("Bank: otrzymałem od klienta żądanie wystawienia banknotu.");

                Destination replyTo = objectMessage.getJMSReplyTo();
                jmsTemplate.send(replyTo, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();
                        UnblindingKeysRequest unblindingKeysRequest = new UnblindingKeysRequest();
                        replyMessage.setObject(unblindingKeysRequest);
                        replyMessage.setJMSReplyTo(cashGenerationQueue);

                        System.out.println("Bank: wysyłam do klienta żądanie przekazania kluczy.");

                        return replyMessage;
                    }
                });
            }

            if(objectMessage.getObject() instanceof UnblindingKeysArray){

                /**
                 * Tutaj będzie odbywać się magia jak już bank dostanie listę kluczy do banknotów.
                 *
                 * TODO: bank sprawdza 99 wcześniej wylosowanych banknotów i jeżeli wszystko jest OK
                 * TODO: podpisuje i wysyła SignedBanknote do customera
                 *
                 */

                UnblindingKeysArray unblindingKeysArray = (UnblindingKeysArray) objectMessage.getObject();

                System.out.println("Bank: otrzymałem od klienta zestaw kluczy.");

                Destination replyTo = objectMessage.getJMSReplyTo();

                jmsTemplate.send(replyTo, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();
                        SignedBanknote signedBanknote = new SignedBanknote();
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
}
