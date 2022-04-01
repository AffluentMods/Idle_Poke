package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Snorlax extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 30, 1e87, "Increases Snorlax's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 30, 2.5e87, "Increases Snorlax's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 30, 1e87, "Increases Snorlax's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 100, new UpgradeData(1.5, 0), 30, 8e87, "Increases Snorlax's DPS by 150%");

    // 30 Snorlax = Phthalo
    public Snorlax() {
        super(30, "Snorlax", new BigDecimal(Double.toString(1.000e85)).toBigInteger(), 5.241e70, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}