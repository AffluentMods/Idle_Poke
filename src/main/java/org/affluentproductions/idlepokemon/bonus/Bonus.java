package org.affluentproductions.idlepokemon.bonus;

public class Bonus {

    private final double value;
    private final BonusType bonusType;

    public Bonus(double value, BonusType bonusType) {
        this.value = value;
        this.bonusType = bonusType;
    }

    public double getValue() {
        return value;
    }

    public BonusType getBonusType() {
        return bonusType;
    }
}