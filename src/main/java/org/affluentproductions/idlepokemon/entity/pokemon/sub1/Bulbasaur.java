package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Bulbasaur extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 5, 40000, "Increases Bulbasaur's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 5, 1e5, "Increases Bulbasaur's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 5, 4e5, "Increases Bulbasaur's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(0.25, 0), -1, 3.2e6, "Increases total DPS by 25%");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData(0, 0, 0.005), 5, 3.2e7, "Increases Click Damage by 0.5% of total DPS");

    // The Wandering Fisherman
    public Bulbasaur() {
        super(5, "Bulbasaur", 4000, 245, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}