package pl.edu.amu.wmi.customer.services;

import com.google.common.base.Preconditions;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import pl.edu.amu.wmi.common.objects.ShopIdentificationInfoRequest;
import pl.edu.amu.wmi.common.objects.ShopIdentificationInfoResponse;

import javax.jms.*;

/**
 * Created by Tomasz on 2015-01-19.
 **/
public class SendIdentificationService implements MessageListener {

    private JmsTemplate jmsTemplate;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void onMessage(Message message) {


        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {
            if(objectMessage.getObject() instanceof ShopIdentificationInfoRequest){

                System.out.println("Klient: Otrzymałem od sklepu żądanie wysłania informacji identyfikacyjnej");

                ShopIdentificationInfoRequest shopIdentificationInfoRequest = (ShopIdentificationInfoRequest) objectMessage.getObject();

                jmsTemplate.send(objectMessage.getJMSReplyTo(), new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        ShopIdentificationInfoResponse shopIdentificationInfoResponse = new ShopIdentificationInfoResponse();

                        ObjectMessage replyMessage = session.createObjectMessage();
                        replyMessage.setObject(shopIdentificationInfoResponse);

                        System.out.println("Klient: Wysyłam do sklepu informacje identyfikacyjne");

                        return replyMessage;
                    }
                });
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
