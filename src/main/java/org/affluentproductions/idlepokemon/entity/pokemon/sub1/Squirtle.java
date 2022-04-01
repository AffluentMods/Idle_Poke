package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Squirtle extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(0.2, 0), -1, 2e5, "Increases total DPS by 20%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(0.2, 0), -1, 5e5, "Increases total DPS by 20%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(0.2, 0), -1, 2e6, "Increases total DPS by 20%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(0.2, 0), -1, 1.6e7, "Increases total DPS by 20%");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData(0, 0, 0.005), 6, 1.6e8, "Increases Click Damage by 0.5% of total DPS");

    // Betty
    public Squirtle() {
        super(6, "Squirtle", 20000, 976, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}