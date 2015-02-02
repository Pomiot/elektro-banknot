package pl.edu.amu.wmi.common.objects;

import java.io.Serializable;

/**
 * Created by Tomasz on 2015-01-19.
 */
public class ShopIdentificationInfoRequest implements Serializable {

    BanknotePairToShop banknotePairToShop;

    public ShopIdentificationInfoRequest(BanknotePairToShop banknotePairToShop){

        this.banknotePairToShop = banknotePairToShop;

        // TODO: wygenerować zapytanie do klienta o zestaw stringów identyfikujących

    }

}
