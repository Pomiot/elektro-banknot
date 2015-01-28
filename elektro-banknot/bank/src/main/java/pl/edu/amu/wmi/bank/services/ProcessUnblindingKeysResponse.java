package pl.edu.amu.wmi.bank.services;

import pl.edu.amu.wmi.common.objects.BanknotesToGeneration;
import pl.edu.amu.wmi.common.objects.SignedBanknote;
import pl.edu.amu.wmi.common.objects.UnblindingKeysResponse;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import pl.edu.amu.wmi.common.objects.BanknoteBlinded;
import pl.edu.amu.wmi.common.protocols.blindSignature.RsaBlind;

/**
 * Created by Tomasz on 2015-01-21.
 */
public class ProcessUnblindingKeysResponse {

    /**
     * Klasa otrzymuje od klienta dane do odciemnienia 99 banknotów. Posiada
     * także dostęp do początkowo wysłanych przez klienta banknotów oraz zestawu
     * kluczy banku.
     *
     */
    private UnblindingKeysResponse ukr;
    private BanknotesToGeneration bkg;
    private KeyPair kp;
    private BanknoteBlinded banknoteBlinded;
    private RsaBlind rsaBlind;
    private int number;

    public ProcessUnblindingKeysResponse(UnblindingKeysResponse ukr, BanknotesToGeneration bkg, KeyPair kp, int number) {
        this.ukr = ukr;
        this.bkg = bkg;
        this.kp = kp;
        this.number = number;
        this.banknoteBlinded = null;
        this.rsaBlind = new RsaBlind((RSAPrivateKey) this.kp.getPrivate());
    }

    public SignedBanknote generateSignedBanknote() {
        System.out.println("Bank: Posiadam odślepione banknoty: " + this.ukr.getBanknoteUnblidedList().size());
        System.out.println("Bank: Posiadam ciągi zaślepiające: " + this.ukr.getRandomBlinderBanknotesList().size());
        System.out.println("Bank: Posiadam zaslepione bankonty: " + this.bkg.getBlindedBanknotesList().size());
        System.out.println("Bank: Banknot ślepo podpisywany nr: " + this.number);
        this.takeBanknoteToSign();
        this.unblindBanknotes();
        return new SignedBanknote();
    }

    private void takeBanknoteToSign() {
        this.banknoteBlinded = this.bkg.getBlindedBanknotesList().get(number);
        this.bkg.getBlindedBanknotesList().remove(number);
        System.out.println("Bank: Z wszystkich zaslepionych banknotow wyciaglem ten do podpisu");
        System.out.println("Bank: Pozostało banknotów: " + this.bkg.getBlindedBanknotesList().size());
    }

    private void unblindBanknotes() {
        for (int i = 0; i < this.bkg.getBlindedBanknotesList().size(); i++) {
            BanknoteBlinded bb = this.bkg.getBlindedBanknotesList().get(i);
            System.out.println("Zablindowany nominal: "+Arrays.toString(bb.getAmount()));
            this.rsaBlind.setR(this.ukr.getRandomBlinderBanknotesList().get(i));
            System.out.println("Odblindowany nominal: "+Arrays.toString(this.rsaBlind.unblind(bb.getAmount())));
            System.out.println("Orginalny nominal: "+ Arrays.toString(this.ukr.getBanknoteUnblidedList().get(i).getAmount()));
        }
    }

}
