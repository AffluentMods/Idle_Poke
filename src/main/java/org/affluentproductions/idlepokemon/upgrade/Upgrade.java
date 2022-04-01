package org.affluentproductions.idlepokemon.upgrade;

import java.math.BigDecimal;

public class Upgrade {

    private final String upgradeDisplay;
    private final int upgradeID;
    private final int minLevel;
    private final UpgradeData upgradeData;
    private final int affectPokemon;
    private final BigDecimal cost;

    public Upgrade(int upgradeID, int minLevel, UpgradeData upgradeData, int affectPokemon, double cost,
                   String upgradeDisplay) {
        this(upgradeID, minLevel, upgradeData, affectPokemon, BigDecimal.valueOf(cost), upgradeDisplay);
    }

    public Upgrade(int upgradeID, int minLevel, UpgradeData upgradeData, int affectPokemon, BigDecimal cost,
                   String upgradeDisplay) {
        this.upgradeDisplay = upgradeDisplay;
        this.upgradeID = upgradeID;
        this.minLevel = minLevel;
        this.affectPokemon = affectPokemon;
        this.cost = cost;
        this.upgradeData = upgradeData;
    }

    public int getUpgradeID() {
        return upgradeID;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getAffectPokemon() {
        return affectPokemon;
    }

    public UpgradeData getUpgradeData() {
        return upgradeData;
    }

    public String getUpgradeDisplay() {
        return upgradeDisplay;
    }
}