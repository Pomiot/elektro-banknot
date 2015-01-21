package pl.edu.amu.wmi.bank.services;

import pl.edu.amu.wmi.common.objects.BanknotesToGeneration;
import pl.edu.amu.wmi.common.objects.SignedBanknote;
import pl.edu.amu.wmi.common.objects.UnblindingKeysResponse;

import java.security.KeyPair;

/**
 * Created by Tomasz on 2015-01-21.
 */
public class ProcessUnblindingKeysResponse {

    /**
     * Klasa otrzymuje od klienta dane do odciemnienia 99 banknotów. Posiada także dostęp
     * do początkowo wysłanych przez klienta banknotów oraz zestawu kluczy banku.
     *
     */

    public static SignedBanknote generateSignedBanknote(UnblindingKeysResponse ukr, BanknotesToGeneration bkg, KeyPair kp){

        return new SignedBanknote();
    }
}

