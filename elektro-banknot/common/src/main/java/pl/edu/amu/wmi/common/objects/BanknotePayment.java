package pl.edu.amu.wmi.common.objects;

import java.io.Serializable;

/**
 * Created by Tomasz on 2015-02-02.
 */

public class BanknotePayment implements Serializable {

    public ShopIdentificationInfoResponse shopIdentificationInfoResponse;
    public BanknotePairToShop banknotePairToShop;

    public BanknotePayment(ShopIdentificationInfoResponse shopIdentificationInfoResponse, BanknotePairToShop banknotePairToShop) {

        this.shopIdentificationInfoResponse = shopIdentificationInfoResponse;
        this.banknotePairToShop = banknotePairToShop;

        // TODO: dodać podpisany banknot i zestaw identyfikujących stringów od klienta. Cały ten obiekt idzie do banku

    }
}
