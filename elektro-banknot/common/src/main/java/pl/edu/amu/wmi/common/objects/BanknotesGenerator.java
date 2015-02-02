/**
 * Klasa odpowiedzialna za tworzenie banknotów przez Alice
 */
package pl.edu.amu.wmi.common.objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.edu.amu.wmi.common.protocols.blindSignature.RsaBlind;

/**
 *
 * @author Patryk
 */
public class BanknotesGenerator implements Serializable {

    private final List<Banknote> banknotesList = new ArrayList<>();
    private final List<BanknoteBlinded> banknotesBlindedList = new ArrayList<>();
    private final byte[] customerId;
    private final List<byte[]> randomBlinderBanknotesList = new ArrayList<>();
    private final List<BanknoteUnblinded> banknotesUnblidedList = new ArrayList<>();
    private RsaBlind rsaBlind;
    //Banknot podpisany z banku
    private int numberFromBank;
    private SignedBanknote signedBanknote;

    //Para podpis banknot
    private BanknotePairToShop banknotePairToShop;

    public BanknotePairToShop getBanknotePairToShop() {
        return banknotePairToShop;
    }

    public void setSignedBanknote(SignedBanknote signedBanknote) {
        this.signedBanknote = this.unblindSignedBanknote(signedBanknote);
        this.prepareBanknotePairToShop();
    }

    public SignedBanknote getSignedBanknote() {
        return signedBanknote;
    }

    public BanknotesGenerator(byte[] customerId) {
        this.customerId = customerId;
    }

    public void banknotesGenerate(String amount) {
        for (int i = 0; i < 100; i++) {
            this.banknotesList.add(new Banknote(amount, this.customerId));
        }
    }

    public BanknotesToGeneration getBanknotesBlindedList() {
        return new BanknotesToGeneration(this.banknotesBlindedList);
    }

    public byte[] banknoteInBytes() {
        Banknote ban = this.banknotesList.get(0);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            bytes.write(ban.getAmount().getBytes());

            bytes.write(ban.getUniquenessString());
            for (byte[] leftID : ban.getLeftIdBanknoteFromIdCustomerRandom1List()) {
                bytes.write(leftID);
            }
            for (byte[] leftID : ban.getLeftIdBanknoteFromIdCustomerHashList()) {
                bytes.write(leftID);
            }
            for (byte[] rightID : ban.getRightIdBanknoteFromIdCustomerRandom1List()) {
                bytes.write(rightID);
            }
            for (byte[] rightID : ban.getRightIdBanknoteFromIdCustomerHashList()) {
                bytes.write(rightID);
            }
        } catch (IOException ex) {
            Logger.getLogger(BanknotesGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bytes.toByteArray();

    }

    public byte[] banknotesInBytes() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        for (Banknote banknote : this.banknotesList) {
            try {
                bytes.write(banknote.getAmount().getBytes());

                bytes.write(banknote.getUniquenessString());
//                for (byte[] leftID : banknote.getLeftIdBanknoteFromIdCustomerList()) {
//                    bytes.write(leftID);
//                }
                for (byte[] leftID : banknote.getLeftIdBanknoteFromIdCustomerRandom1List()) {
                    bytes.write(leftID);
                }
//                for (byte[] leftID : banknote.getLeftIdBanknoteFromIdCustomerRandom2List()) {
//                    bytes.write(leftID);
//                }
                for (byte[] leftID : banknote.getLeftIdBanknoteFromIdCustomerHashList()) {
                    bytes.write(leftID);
                }
//                for (byte[] rightID : banknote.getRightIdBanknoteFromIdCustomerList()) {
//                    bytes.write(rightID);
//                }
                for (byte[] rightID : banknote.getRightIdBanknoteFromIdCustomerRandom1List()) {
                    bytes.write(rightID);
                }
//                for (byte[] rightID : banknote.getRightIdBanknoteFromIdCustomerRandom2List()) {
//                    bytes.write(rightID);
//                }
                for (byte[] rightID : banknote.getRightIdBanknoteFromIdCustomerHashList()) {
                    bytes.write(rightID);
                }
            } catch (IOException ex) {
                Logger.getLogger(BanknotesGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bytes.toByteArray();
    }

    public void blindBanknotesInBytes(RSAPublicKey rsaPublicKey) {
        this.rsaBlind = new RsaBlind(rsaPublicKey);
        rsaBlind.prepareBlind();

        for (Banknote banknot : this.banknotesList) {
            rsaBlind.generateNewRandom();
            this.randomBlinderBanknotesList.add(rsaBlind.getR());

            List<byte[]> randomLeft = new ArrayList<>();
            for (byte[] leftID : banknot.getLeftIdBanknoteFromIdCustomerRandom1List()) {
                randomLeft.add(rsaBlind.blind(leftID));
            }

            List<byte[]> randomLeftHash = new ArrayList<>();
            for (byte[] leftID : banknot.getLeftIdBanknoteFromIdCustomerHashList()) {
                randomLeftHash.add(rsaBlind.blind(leftID));
            }

            List<byte[]> randomRight = new ArrayList<>();
            for (byte[] rightID : banknot.getRightIdBanknoteFromIdCustomerRandom1List()) {
                randomRight.add(rsaBlind.blind(rightID));
            }
            List<byte[]> randomRightHash = new ArrayList<>();
            for (byte[] rightID : banknot.getRightIdBanknoteFromIdCustomerHashList()) {
                randomRightHash.add(rsaBlind.blind(rightID));
            }

            try {
                this.banknotesBlindedList.add(new BanknoteBlinded(
                        rsaBlind.blind(banknot.getAmount().getBytes("UTF8")),
                        rsaBlind.blind(banknot.getUniquenessString()),
                        randomLeft,
                        randomLeftHash,
                        randomRight,
                        randomRightHash));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(BanknotesGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public UnblindingKeysResponse getUnblindedBanknotes(int numberFromBank) {
        this.numberFromBank = numberFromBank;
        System.out.println("Klient: Przygotowuje odtajnione banknoty po za " + numberFromBank);

        for (int i = 0; i < this.banknotesList.size(); i++) {
            if (i != numberFromBank) {
                this.banknotesUnblidedList.add(new BanknoteUnblinded(
                        this.banknotesList.get(i).getAmount().getBytes(),
                        this.banknotesList.get(i).getUniquenessString(),
                        this.banknotesList.get(i).getLeftIdBanknoteFromIdCustomerRandom1List(),
                        this.banknotesList.get(i).getLeftIdBanknoteFromIdCustomerRandom2List(),
                        this.banknotesList.get(i).getLeftIdBanknoteFromIdCustomerHashList(),
                        this.banknotesList.get(i).getRightIdBanknoteFromIdCustomerRandom1List(),
                        this.banknotesList.get(i).getRightIdBanknoteFromIdCustomerRandom2List(),
                        this.banknotesList.get(i).getRightIdBanknoteFromIdCustomerHashList()));
            }
        }
        List<byte[]> temp = this.randomBlinderBanknotesList;
        temp.remove(numberFromBank);
        return new UnblindingKeysResponse(this.banknotesUnblidedList, temp);
    }
 
    private void prepareBanknotePairToShop(){
        BanknoteUnblinded bu = this.banknotesUnblidedList.get(this.numberFromBank);
        bu.setLeftIdBanknoteFromIdCustomerRandom2List(null);
        bu.setRightIdBanknoteFromIdCustomerRandom2List(null);
        
        this.banknotePairToShop = new BanknotePairToShop(this.signedBanknote, bu);
    }
    private SignedBanknote unblindSignedBanknote(SignedBanknote signedBanknote){
        this.rsaBlind.setR(this.randomBlinderBanknotesList.get(this.numberFromBank));

        List<byte[]> leftRandom = new ArrayList<>();
        for(byte[] bb: signedBanknote.getLeftIdBanknoteFromIdCustomerRandom1List()){
            leftRandom.add(this.rsaBlind.unblindSign(bb));
        }
        List<byte[]> leftHash = new ArrayList<>();
        for(byte[] bb: signedBanknote.getLeftIdBanknoteFromIdCustomerHashList()){
            leftHash.add(this.rsaBlind.unblindSign(bb));
        }
        List<byte[]> rightRandom = new ArrayList<>();
        for(byte[] bb: signedBanknote.getRightIdBanknoteFromIdCustomerRandom1List()){
            rightRandom.add(this.rsaBlind.unblindSign(bb));
        }
        List<byte[]> rightHash = new ArrayList<>();
        for(byte[] bb: signedBanknote.getRightIdBanknoteFromIdCustomerHashList()){
            rightHash.add(this.rsaBlind.unblindSign(bb));
        }
        return new SignedBanknote(
                this.rsaBlind.unblindSign(signedBanknote.getAmount()), 
                this.rsaBlind.unblindSign(signedBanknote.getUniquenessString()), 
                leftRandom, 
                leftHash, 
                rightRandom, 
                rightHash);
    }
}
