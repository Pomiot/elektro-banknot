package pl.edu.amu.wmi.bank.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.amu.wmi.bank.billing.Accounts;
import pl.edu.amu.wmi.common.objects.BanknotePayment;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by Tomasz on 2015-02-02.
 */
public class GettingCashFromShopService implements MessageListener
{

    @Autowired
    Accounts accounts;

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public void onMessage(Message message) {

        System.out.println("Bank: dostałem hajs ze sklepu.");

        Preconditions.checkArgument(message instanceof ObjectMessage);

        ObjectMessage objectMessage = (ObjectMessage) message;

        try {
            BanknotePayment banknotePayment = (BanknotePayment) objectMessage.getObject();

            if(Accounts.uniquenessStrings.indexOf(banknotePayment.banknotePairToShop.getBanknoteUnblinded().getUniquenessString()) < 0){
                Accounts.uniquenessStrings.add(banknotePayment.banknotePairToShop.getBanknoteUnblinded().getUniquenessString());
                System.out.println("Wszystko wyglada si, dodaje ID banknotu do listy uzytych.");
            }
            else{
                System.out.println("KTOS TU COS OSZWABIL!");
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
