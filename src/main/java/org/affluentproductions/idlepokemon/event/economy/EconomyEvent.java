package org.affluentproductions.idlepokemon.event.economy;

import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.event.Event;

import java.math.BigInteger;

public class EconomyEvent extends Event {

    private final EcoUser ecoUser;
    private final BigInteger oldBalance;
    private final BigInteger newBalance;

    public EconomyEvent(String userId, EcoUser ecoUser, BigInteger oldBalance, BigInteger newBalance) {
        super(userId);
        this.ecoUser = ecoUser;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
    }

    public EcoUser getEcoUser() {
        return ecoUser;
    }

    public BigInteger getOldBalance() {
        return oldBalance;
    }

    public BigInteger getNewBalance() {
        return newBalance;
    }
}