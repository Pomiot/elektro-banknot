package pl.edu.amu.wmi.bank.services;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by Tomasz on 2015-02-02.
 */
public class GettingCashFromShopService implements MessageListener
{
    @Override
    public void onMessage(Message message) {

        System.out.println("Bank: dostałem hajs ze sklepu.");

    }
}
