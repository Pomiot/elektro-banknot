package pl.edu.amu.wmi.customer;

import java.security.PublicKey;

/**
 * Created by Tomasz on 2015-01-21.
 */
public class BankPublicKey {

    private PublicKey bankPublicKey;

    public PublicKey getBankPublicKey() {
        return bankPublicKey;
    }

    public void setBankPublicKey(PublicKey bankPublicKey) {
        this.bankPublicKey = bankPublicKey;
    }
}
