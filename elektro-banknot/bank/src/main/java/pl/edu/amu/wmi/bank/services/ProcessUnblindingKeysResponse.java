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
import pl.edu.amu.wmi.common.protocols.hashCommitmentscheme.HashCommitmentScheme;
import pl.edu.amu.wmi.common.protocols.secretSharing.secretSharing;

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
        System.out.println("Bank: Rozpoczynam sprawdzanie poprawoności banknotowów");
        if (this.checkSize()) {
            if (this.checkAmount() && this.checkUniquenessString()
                    && this.checkLeftRandom1() && this.checkLeftHash()
                    && this.checkRightRandom1()) {
                if (this.checkRightHash() && this.checkGenerateLeftHash()
                        && this.checkGenerateRightHash() & this.checkCorrectId()) {
                    return this.prepareSignedBanknote();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
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
//            System.out.println("DUPSKO: " + Arrays.toString(this.rsaBlind.unblind(bb.getUniquenessString())));
            this.bu.add(new BanknoteUnblinded(
                    this.rsaBlind.unblind(bb.getAmount()),
                    this.rsaBlind.unblind(bb.getUniquenessString()),
                    bbLeftIdRandom,
                    null,
                    bbLeftIdHash,
                    bbRightRandom,
                    null,
                    bbRightHash));

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
//                System.out.println(Arrays.toString(this.bu.get(i).getUniquenessString()));
//                System.out.println(Arrays.toString(this.ukr.getBanknoteUnblidedList().get(i).getUniquenessString()));
//                System.out.println(i+" 1");
                return false;
            }
        }
        for (int i = 0; i < this.bu.size(); i++) {
            for (int j = 0; j < this.bu.size(); j++) {
                if (i != j) {
                    if (Arrays.equals(this.bu.get(i).getUniquenessString(), this.bu.get(j).getUniquenessString())) {
                        {
//                            System.out.println(Arrays.toString(this.bu.get(i).getUniquenessString()));
//                            System.out.println(Arrays.toString(this.bu.get(j).getUniquenessString()));
//                            System.out.println(i + " " + j + " 2");
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean checkLeftRandom1() {

        for (int i = 0; i < this.bu.size(); i++) {
            List<byte[]> bu1 = this.bu.get(i).getLeftIdBanknoteFromIdCustomerRandom1List();
            List<byte[]> ukr1 = this.ukr.getBanknoteUnblidedList().get(i).getLeftIdBanknoteFromIdCustomerRandom1List();
            for (int j = 0; j < bu1.size(); j++) {
                if (!Arrays.equals(bu1.get(j), ukr1.get(j))) {
                    System.out.println(Arrays.toString(bu1.get(j)));
                    System.out.println(Arrays.toString(ukr1.get(j)));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkLeftHash() {
        for (int i = 0; i < this.bu.size(); i++) {
            List<byte[]> bu1 = this.bu.get(i).getLeftIdBanknoteFromIdCustomerHashList();
            List<byte[]> ukr1 = this.ukr.getBanknoteUnblidedList().get(i).getLeftIdBanknoteFromIdCustomerHashList();
            for (int j = 0; j < bu1.size(); j++) {
//                    System.out.println(Arrays.toString(bu1.get(j)));
//                    System.out.println(Arrays.toString(ukr1.get(j)));
                if (!Arrays.equals(bu1.get(j), ukr1.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRightRandom1() {
        for (int i = 0; i < this.bu.size(); i++) {
            List<byte[]> bu1 = this.bu.get(i).getRightIdBanknoteFromIdCustomerRandom1List();
            List<byte[]> ukr1 = this.ukr.getBanknoteUnblidedList().get(i).getRightIdBanknoteFromIdCustomerRandom1List();
            for (int j = 0; j < bu1.size(); j++) {
                if (!Arrays.equals(bu1.get(j), ukr1.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRightHash() {
        for (int i = 0; i < this.bu.size(); i++) {
            List<byte[]> bu1 = this.bu.get(i).getRightIdBanknoteFromIdCustomerHashList();
            List<byte[]> ukr1 = this.ukr.getBanknoteUnblidedList().get(i).getRightIdBanknoteFromIdCustomerHashList();
            for (int j = 0; j < bu1.size(); j++) {
                if (!Arrays.equals(bu1.get(j), ukr1.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkGenerateLeftHash() {

        for (int i = 0; i < this.bu.size(); i++) {
            List<byte[]> random1 = this.ukr.getBanknoteUnblidedList().get(i).getLeftIdBanknoteFromIdCustomerRandom1List();
            List<byte[]> random2 = this.ukr.getBanknoteUnblidedList().get(i).getLeftIdBanknoteFromIdCustomerRandom2List();
            List<byte[]> decision = this.ukr.getBanknoteUnblidedList().get(i).getLeftIdBanknoteFromIdCustomerList();

            for (int j = 0; j < decision.size(); j++) {
                HashCommitmentScheme hash = new HashCommitmentScheme(
                        random1.get(j),
                        random2.get(j),
                        decision.get(j));
                byte[] result = hash.generateHash();
                if (!Arrays.equals(result, this.bu.get(i).getLeftIdBanknoteFromIdCustomerHashList().get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkGenerateRightHash() {

        for (int i = 0; i < this.bu.size(); i++) {
            List<byte[]> random1 = this.ukr.getBanknoteUnblidedList().get(i).getRightIdBanknoteFromIdCustomerRandom1List();
            List<byte[]> random2 = this.ukr.getBanknoteUnblidedList().get(i).getRightIdBanknoteFromIdCustomerRandom2List();
            List<byte[]> decision = this.ukr.getBanknoteUnblidedList().get(i).getRightIdBanknoteFromIdCustomerList();

            for (int j = 0; j < decision.size(); j++) {
                HashCommitmentScheme hash = new HashCommitmentScheme(
                        random1.get(j),
                        random2.get(j),
                        decision.get(j));
                byte[] result = hash.generateHash();
                if (!Arrays.equals(result, this.bu.get(i).getRightIdBanknoteFromIdCustomerHashList().get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkCorrectId(){
        List<byte[]> id = new ArrayList<>();
        secretSharing secret = new secretSharing();
        for (int i=0;i<this.bu.size();i++){
            List<byte[]> left1 = this.ukr.getBanknoteUnblidedList().get(i).getLeftIdBanknoteFromIdCustomerList();
            List<byte[]> right1 = this.ukr.getBanknoteUnblidedList().get(i).getRightIdBanknoteFromIdCustomerList();
            
            for (int j=0;j<left1.size();j++){
                secret.setRandom1(left1.get(j));
                secret.setResult(right1.get(j));
                secret.generateMessage();
                id.add(secret.getByteMessage());
            }
        }
        for (int i=0;i<id.size()-1;i++){
            if (!Arrays.equals(id.get(i), id.get(i+1))){
                return false;
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
