package pl.edu.amu.wmi.bank.services;

import pl.edu.amu.wmi.common.objects.BanknotesToGeneration;
import pl.edu.amu.wmi.common.objects.UnblindingKeysRequest;

/**
 * Created by Tomasz on 2015-01-21.
 */
public class ProcessBanknotesToGeneration {

    /**
     * Klasa otrzymuje od klienta początkową paczkę 100 banknotów i tworzy
     * żądanie przesłania danych do odciemnienia 99 spośród otrzymanych
     * banknotów.
     *
     */
    public static UnblindingKeysRequest generateUnblindingKeysRequest(BanknotesToGeneration btg) {
        if (btg.getBlindedBanknotesList().size() == 100) {
            System.out.println("Otrzymałem banknotów: " + btg.getBlindedBanknotesList().size());
            return new UnblindingKeysRequest();
        } else {
            System.out.println("Zrobiłeś mnie w chuja");
            return null;
        }
    }

}
