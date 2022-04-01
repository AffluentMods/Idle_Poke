package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Espeon extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 17, 3.6e14, "Increases Espeon's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 17, 9e14, "Increases Espeon's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 17, 3.6e15, "Increases Espeon's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1.5, 0), 17, 2.88e16, "Increases Espeon's DPS by 150%");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData("3"), 17, 2.88e17, "Increases Critical Chance by 3%");

    // Referi Jerator
    public Espeon() {
        super(17, "Espeon", new BigDecimal(Double.toString(3.600e13)).toBigInteger(), 2.000e10, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}