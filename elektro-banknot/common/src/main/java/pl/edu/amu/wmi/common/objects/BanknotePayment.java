package pl.edu.amu.wmi.common.objects;

import java.io.Serializable;

/**
 * Created by Tomasz on 2015-02-02.
 */

public class BanknotePayment implements Serializable {

    private ShopIdentificationInfoResponse shopIdentificationInfoResponse;
    private Banknote savedBanknote;

    public BanknotePayment(ShopIdentificationInfoResponse shopIdentificationInfoResponse, Banknote savedBanknote) {

        this.shopIdentificationInfoResponse = shopIdentificationInfoResponse;
        this.savedBanknote = savedBanknote;

        // TODO: dodać podpisany banknot i zestaw identyfikujących stringów od klienta. Cały ten obiekt idzie do banku

    }
}
