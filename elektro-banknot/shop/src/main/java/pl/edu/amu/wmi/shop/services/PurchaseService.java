package pl.edu.amu.wmi.shop.services;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.ShopIdentificationInfoRequest;
import pl.edu.amu.wmi.common.objects.ShopIdentificationInfoResponse;

import javax.jms.*;
import pl.edu.amu.wmi.common.objects.BanknotePairToShop;

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
    public void onMessage(final Message message) {

        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {
            if (objectMessage.getObject() instanceof BanknotePairToShop) {
                System.out.println("Sklep: otrzymałem wiadomość. Klient na kolejce " + objectMessage.getJMSReplyTo() + " chce zakupić " + objectMessage.getStringProperty("item"));
                BanknotePairToShop banknotePairToShop = (BanknotePairToShop) objectMessage.getObject();
                System.out.println("ASS: " + Arrays.toString(banknotePairToShop.getSignedBanknote().getAmount()));
                System.out.println("ASS2: "+Arrays.toString(banknotePairToShop.getBanknoteUnblinded().getAmount()));
                jmsTemplate.send(objectMessage.getJMSReplyTo(), new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();

                        ShopIdentificationInfoRequest shopIdentificationInfoRequest = new ShopIdentificationInfoRequest();
                        replyMessage.setObject(shopIdentificationInfoRequest);
                        replyMessage.setJMSReplyTo(purchaseQueue);

                        System.out.println("Sklep: wysyłam do klienta żądanie otrzymania informacji identyfikującej");

                        return replyMessage;
                    }
                });
            }

            if (objectMessage.getObject() instanceof ShopIdentificationInfoResponse) {
                System.out.println("Sklep: otrzymałem od klienta informacje identyfikujące");
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
