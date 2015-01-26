package pl.edu.amu.wmi.customer.UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import pl.edu.amu.wmi.common.UI.UIInterface;

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
