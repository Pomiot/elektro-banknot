package pl.edu.amu.wmi.common.objects;

/**
 * klucze, które Alice wysyła do banku.
 */
import java.io.Serializable;
import java.util.List;

/**
 * Created by Tomasz on 2015-01-18.
 */
public class UnblindingKeysResponse implements Serializable {

    private List<BanknoteUnblinded> banknoteUnblidedList;
    private final List<byte[]> randomBlinderBanknotesList;

    public UnblindingKeysResponse(List<BanknoteUnblinded> banknoteUnblidedList, List<byte[]> randomBlinderBanknotesList) {
        this.banknoteUnblidedList = banknoteUnblidedList;
        this.randomBlinderBanknotesList = randomBlinderBanknotesList;
    }

    public List<BanknoteUnblinded> getBanknoteUnblidedList() {
        return banknoteUnblidedList;
    }

    public List<byte[]> getRandomBlinderBanknotesList() {
        return randomBlinderBanknotesList;
    }
    

}
