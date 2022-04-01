package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Weavile extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 13, 6.5e10, "Increases Weavile's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0.0), 13, 1.625e11, "Increases Weavile's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0.0), 13, 6.5e11, "Increases Weavile's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1.5, 0), 13, 5.2e12, "Increases Weavile's DPS by 150%");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData("3"), 13, 5.2e13, "Increases Critical Chance by 3%");

    public Weavile() {
        super(13, "Weavile", 6500000000L, 17010000, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}