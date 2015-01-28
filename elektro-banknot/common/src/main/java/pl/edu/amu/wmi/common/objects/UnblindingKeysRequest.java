package pl.edu.amu.wmi.common.objects;

/**
 * wysyła bank do Alice w celu uzyskania od niej kluczy do tych 99 bnaknotów
 * żeby sprawdzić czy są si..
 */
import java.io.Serializable;
import java.util.Random;

/**
 * Created by Tomasz on 2015-01-18.
 */
public class UnblindingKeysRequest implements Serializable {

    private int number;

    public UnblindingKeysRequest() {
        this.generateNumber();
        System.out.println("Bank: Wylosowalem numer "+this.number);
    }

    private void generateNumber() {
        Random generator = new Random();
        this.number = generator.nextInt(100);
    }

    public int getNumber() {
        return number;
    }

}
