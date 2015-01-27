/**
 * Klasa odpowiedzialna za tworzenie banknotów przez Alice
 */
package pl.edu.amu.wmi.common.objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class BanknotesGenerator {

    private final List<Banknote> banknotesList = new ArrayList<>();
    private final List<BanknoteBlinded> banknotesBlindedList = new ArrayList<>();
    private final byte[] customerId;
    private final List<byte[]> randomBlinderBanknotesList = new ArrayList<>();

    public BanknotesGenerator(byte[] customerId) {
        this.customerId = customerId;
    }

    public void banknotesGenerate(String amount) {
        for (int i = 0; i < 99; i++) {
            this.banknotesList.add(new Banknote(amount, this.customerId));
        }
    }

    public List<BanknoteBlinded> getBanknotesBlindedList() {
        return banknotesBlindedList;
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

    public List<Integer> banknoteConfigData() {
        List<Integer> list = new ArrayList<>();
        Banknote banknote = this.banknotesList.get(0);
        list.add(banknote.getAmount().getBytes().length);
        list.add(banknote.getUniquenessString().length);
        list.add(banknote.getLeftIdBanknoteFromIdCustomerList().get(0).length);
        list.add(banknote.getRightIdBanknoteFromIdCustomerList().get(0).length);
        list.add(banknote.getLeftIdBanknoteFromIdCustomerRandom1List().get(0).length);
        list.add(banknote.getLeftIdBanknoteFromIdCustomerRandom2List().get(0).length);
        list.add(banknote.getLeftIdBanknoteFromIdCustomerHashList().get(0).length);
        list.add(banknote.getRightIdBanknoteFromIdCustomerRandom1List().get(0).length);
        list.add(banknote.getRightIdBanknoteFromIdCustomerRandom2List().get(0).length);
        list.add(banknote.getRightIdBanknoteFromIdCustomerHashList().get(0).length);
        return list;
    }

    public void blindBanknotesInBytes(RSAPublicKey rsaPublicKey) {
        RsaBlind rsaBlind = new RsaBlind(rsaPublicKey);
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

            this.banknotesBlindedList.add(new BanknoteBlinded(
                    rsaBlind.blind(banknot.getAmount().getBytes()),
                    rsaBlind.blind(banknot.getUniquenessString()),
                    randomLeft,
                    randomLeftHash,
                    randomRight,
                    randomRightHash));
        }
    }
}
