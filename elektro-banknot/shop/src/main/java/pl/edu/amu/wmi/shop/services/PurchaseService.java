package pl.edu.amu.wmi.shop.services;

import com.google.common.base.Preconditions;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.BanknotePairToShop;
import pl.edu.amu.wmi.common.objects.BanknotePayment;
import pl.edu.amu.wmi.common.objects.ShopIdentificationInfoRequest;
import pl.edu.amu.wmi.common.objects.ShopIdentificationInfoResponse;

import javax.jms.*;
import java.util.Arrays;


/**
 * Created by Tomasz on 2015-01-18.
 */
public class PurchaseService implements MessageListener {

    BanknotePairToShop savedBanknotePairToShop;

    JmsTemplate jmsTemplate;

    Destination purchaseQueue;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    Destination bankPaymentQueue;

    public void setBankPaymentQueue(Destination bankPaymentQueue) {
        this.bankPaymentQueue = bankPaymentQueue;
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

                final BanknotePairToShop banknotePairToShop = (BanknotePairToShop) objectMessage.getObject();

                savedBanknotePairToShop = banknotePairToShop;

                System.out.println("ASS: " + Arrays.toString(banknotePairToShop.getSignedBanknote().getAmount()));
                System.out.println("ASS2: " + Arrays.toString(banknotePairToShop.getBanknoteUnblinded().getAmount()));

                jmsTemplate.send(objectMessage.getJMSReplyTo(), new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();

                        ShopIdentificationInfoRequest shopIdentificationInfoRequest = new ShopIdentificationInfoRequest(banknotePairToShop);
                        replyMessage.setObject(shopIdentificationInfoRequest);
                        replyMessage.setJMSReplyTo(purchaseQueue);

                        System.out.println("Sklep: wysyłam do klienta żądanie otrzymania informacji identyfikującej");

                        return replyMessage;
                    }
                });
            }}
        catch(JMSException e) {
            e.printStackTrace();
        }

        try{
            if (objectMessage.getObject() instanceof ShopIdentificationInfoResponse) {

                System.out.println("Sklep: otrzymałem od klienta informacje identyfikujące");

                final ShopIdentificationInfoResponse shopIdentificationInfoResponse = (ShopIdentificationInfoResponse) objectMessage.getObject();

                jmsTemplate.send(bankPaymentQueue, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ObjectMessage replyMessage = session.createObjectMessage();

                        BanknotePayment banknotePayment = new BanknotePayment(shopIdentificationInfoResponse,savedBanknotePairToShop);

                        replyMessage.setObject(banknotePayment);

                        System.out.println("Sklep: wysyłam otrzymant banknot do banku.");

                        return replyMessage;
                    }
                });}
            }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
