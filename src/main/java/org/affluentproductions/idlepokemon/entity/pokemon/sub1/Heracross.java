package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Heracross extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 11, 1e9, "Increases Heracross's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0.0), 11, 2.5e9, "Increases Heracross's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0.0), 11, 1e10, "Increases Heracross's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1, 0), 11, 8e10, "Increases Heracross's DPS by 100%");

    // Natalia
    public Heracross() {
        super(11, "Heracross", 100000000L, 782000, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}