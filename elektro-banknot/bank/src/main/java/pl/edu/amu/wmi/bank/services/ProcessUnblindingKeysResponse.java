package pl.edu.amu.wmi.bank.services;

import pl.edu.amu.wmi.common.objects.BanknotesToGeneration;
import pl.edu.amu.wmi.common.objects.SignedBanknote;
import pl.edu.amu.wmi.common.objects.UnblindingKeysResponse;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pl.edu.amu.wmi.common.objects.BanknoteBlinded;
import pl.edu.amu.wmi.common.objects.BanknoteUnblinded;
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
    private RSAPrivateKey privKey;
    private RSAPublicKey publicKey;
    private BanknoteBlinded banknoteBlinded;
    private RsaBlind rsaBlind;
    private int number;
    private final List<BanknoteUnblinded> bu = new ArrayList<>();

    public ProcessUnblindingKeysResponse(UnblindingKeysResponse ukr, BanknotesToGeneration bkg, RSAPrivateKey privKey, RSAPublicKey publicKey, int number) {
        this.ukr = ukr;
        this.bkg = bkg;
        this.privKey = privKey;
        this.publicKey = publicKey;
        this.banknoteBlinded = null;
        this.rsaBlind = new RsaBlind(this.privKey, this.publicKey);
        this.number = number;
        this.rsaBlind.prepareBlind();
    }

    public SignedBanknote generateSignedBanknote() {
        System.out.println("Bank: Posiadam odślepione banknoty: " + this.ukr.getBanknoteUnblidedList().size());
        System.out.println("Bank: Posiadam ciągi zaślepiające: " + this.ukr.getRandomBlinderBanknotesList().size());
        System.out.println("Bank: Posiadam zaslepione bankonty: " + this.bkg.getBlindedBanknotesList().size());
        System.out.println("Bank: Banknot ślepo podpisywany nr: " + this.number);
        this.takeBanknoteToSign();
        this.unblindBanknotes();
        if (this.checkSize()) {
            System.out.println("Bank: Rozpoczynam sprawdzanie poprawoności banknotowów");
            System.out.println(this.checkAmount());
            System.out.println(this.checkUniquenessString());
        } else {
            return null;
        }
        return this.prepareSignedBanknote();
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
            this.rsaBlind.setR(this.ukr.getRandomBlinderBanknotesList().get(i));
            List<byte[]> bbLeftIdRandom = new ArrayList<>();
            for (byte[] bytes : bb.getLeftIdBanknoteFromIdCustomerRandom1List()) {
                bbLeftIdRandom.add(this.rsaBlind.unblind(bytes));
            }
            List<byte[]> bbLeftIdHash = new ArrayList<>();
            for (byte[] bytes : bb.getLeftIdBanknoteFromIdCustomerHashList()) {
                bbLeftIdHash.add(this.rsaBlind.unblind(bytes));
            }
            List<byte[]> bbRightRandom = new ArrayList<>();
            for (byte[] bytes : bb.getRightIdBanknoteFromIdCustomerRandom1List()) {
                bbRightRandom.add(this.rsaBlind.unblind(bytes));
            }
            List<byte[]> bbRightHash = new ArrayList<>();
            for (byte[] bytes : bb.getRightIdBanknoteFromIdCustomerHashList()) {
                bbRightHash.add(this.rsaBlind.unblind(bytes));
            }
            this.bu.add(new BanknoteUnblinded(
                    this.rsaBlind.unblind(bb.getAmount()),
                    this.rsaBlind.unblind(bb.getUniquenessString()),
                    bbLeftIdRandom,
                    null,
                    bbLeftIdHash,
                    bbRightRandom,
                    bbRightHash,
                    null));

        }
    }

    private boolean checkSize() {
        return (this.bu.size() == this.bkg.getBlindedBanknotesList().size()) && (this.bu.size() == 99);
    }

    private boolean checkAmount() {
        byte[] amount = this.bu.get(0).getAmount();
        for (int i = 0; i < this.bu.size(); i++) {
            if (Arrays.equals(this.bu.get(i).getAmount(), this.ukr.getBanknoteUnblidedList().get(i).getAmount())) {
                if (!Arrays.equals(amount, this.bu.get(i).getAmount())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean checkUniquenessString() {
        for (int i = 0; i < this.bu.size(); i++) {
            if (!Arrays.equals(this.bu.get(i).getUniquenessString(), this.ukr.getBanknoteUnblidedList().get(i).getUniquenessString())) {
                return false;
            }
        }
        for (int i = 0; i < this.bu.size(); i++) {
            for (int j = 0; j < this.bu.size(); j++) {
                if ((i != j) && (!Arrays.equals(this.bu.get(i).getUniquenessString(), this.bu.get(j).getUniquenessString()))) {
                    return false;
                }
            }
        }
        return true;
    }

    private SignedBanknote prepareSignedBanknote() {
        List<byte[]> leftRandom = new ArrayList<>();
        for (byte[] bb : this.banknoteBlinded.getLeftIdBanknoteFromIdCustomerRandom1List()) {
            leftRandom.add(this.rsaBlind.sign(bb));
        }
        List<byte[]> leftHash = new ArrayList<>();
        for (byte[] bb : this.banknoteBlinded.getLeftIdBanknoteFromIdCustomerHashList()) {
            leftHash.add(this.rsaBlind.sign(bb));
        }
        List<byte[]> rightRandom = new ArrayList<>();
        for (byte[] bb : this.banknoteBlinded.getRightIdBanknoteFromIdCustomerRandom1List()) {
            rightRandom.add(this.rsaBlind.sign(bb));
        }
        List<byte[]> rightHash = new ArrayList<>();
        for (byte[] bb : this.banknoteBlinded.getRightIdBanknoteFromIdCustomerHashList()) {
            rightHash.add(this.rsaBlind.sign(bb));
        }
        return new SignedBanknote(
                this.rsaBlind.sign(this.banknoteBlinded.getAmount()),
                this.rsaBlind.sign(this.banknoteBlinded.getUniquenessString()),
                leftRandom,
                leftHash,
                rightRandom,
                rightHash);
    }
}
