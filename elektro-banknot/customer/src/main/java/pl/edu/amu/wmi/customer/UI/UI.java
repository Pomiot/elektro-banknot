package pl.edu.amu.wmi.customer.UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.edu.amu.wmi.common.UI.UIInterface;
import pl.edu.amu.wmi.common.cryptography.RSA;
import pl.edu.amu.wmi.common.protocols.blindSignature.RsaBlind;

public class UI implements UIInterface {

    private final String GENERATE_BILL = "Bill";

    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void prepareUI() {
//        clearConsole();
        System.out.println("---| Customer Module Application |---");
        System.out.println("---| Module Allice               |---");
        System.out.println("---| --------------------------- |---");
        System.out.println("---| Enter " + GENERATE_BILL + " to generate bills   |---");
        System.out.println("---| Enter " + EXIT + " to quit      |---");
    }

    @Override
    public void start() {
        boolean isRunning = true;

        String line = new String();

        while (isRunning) {
            prepareUI();
            try {
                line = bufferedReader.readLine();

                switch (line) {
                    case GENERATE_BILL: {
                        System.out.println("Active module bills creator");
//                        RSATesting();
                        break;
                    }
                    case EXIT: {
                        bufferedReader.close();
                        isRunning = false;
                        break;
                    }
                    default: {
                        System.out.println("Unknow operation");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
//RSABlind testing method
    private void RSATesting() {
        RSA rsa = new RSA();
        RsaBlind blind;
        try {
            rsa.GeneratePairKey();
            blind = new RsaBlind(rsa);
            BigInteger blindMessage = blind.blind("ass");
            BigInteger blindSign  = blind.sign(blindMessage);
            BigInteger blindUnblind = blind.unblind(blindSign);
            blind.verify("ass", blindUnblind);
            
        } catch (NoSuchAlgorithmException | NoSuchProviderException | UnsupportedEncodingException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//CLEAR CONSOLE: AT THIS MOMENT DONT WORK IN NETBEANS PROPABLY WORK IN CONSOLE

    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("CLS");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
