/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.amu.wmi.common.objects;

import java.io.Serializable;

/**
 *
 * @author Patryk
 */
public class BanknotePairToShop implements Serializable{
    private SignedBanknote signedBanknote;
    private BanknoteUnblinded banknoteUnblinded;

    public BanknotePairToShop(SignedBanknote signedBanknote, BanknoteUnblinded banknoteUnblinded) {
        this.signedBanknote = signedBanknote;
        this.banknoteUnblinded = banknoteUnblinded;
    }

    public BanknoteUnblinded getBanknoteUnblinded() {
        return banknoteUnblinded;
    }

    public SignedBanknote getSignedBanknote() {
        return signedBanknote;
    }
}
