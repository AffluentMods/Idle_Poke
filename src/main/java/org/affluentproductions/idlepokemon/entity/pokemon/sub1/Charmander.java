package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Charmander extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 7, 1e6, "Increases Charmander's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 7, 2.5e6, "Increases Charmander's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 7, 1e7, "Increases Charmander's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1, 0), 7, 8e7, "Increases Charmander's DPS by 100%");

    // The Masked Samurai
    public Charmander() {
        super(7, "Charmander", 100000, 3725, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}