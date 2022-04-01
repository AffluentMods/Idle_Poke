package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Politoed extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 12, 8e9, "Increases Politoed's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0.0), 12, 2e10, "Increases Politoed's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0.0), 12, 8e10, "Increases Politoed's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1.5, 0), 12, 6.4e11, "Increases Politoed's DPS by 150%");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData(0, 0, 0.005), 12, 6.4e12, "Increases Click Damage by 0.5% of total DPS");

    // Mercedes
    public Politoed() {
        super(12, "Politoed", 800000000L, 3721000, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}