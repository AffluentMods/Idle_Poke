package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Eevee extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1.0, 0), 9, 2.5e7, "Increases Eevee's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1.0, 0), 9, 6.25e7, "Increases Eevee's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1.0, 0), 9, 2.5e8, "Increases Eevee's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1.5, 0), 9, 2e9, "Increases Eevee's DPS by 150%");

    // The Great Forest Seer
    public Eevee() {
        super(9, "Eevee", 2500000L, 47143, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}