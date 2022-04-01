package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Cubone extends Pokemon {

    private static final Upgrade u1 = new Upgrade(1, 10, new UpgradeData(1, 0), 2, 100, "Increases Cubone's DPS by 100%");
    private static final Upgrade u2 = new Upgrade(2, 25, new UpgradeData(1, 0), 2, 1250, "Increases Cubone's DPS by 100%");
    private static final Upgrade u3 = new Upgrade(3, 50, new UpgradeData(1, 0), 2, 5000, "Increases Cubone's DPS by 100%");
    private static final Upgrade u4 = new Upgrade(4, 75, new UpgradeData(1.5, 0), 2, 40000, "Increases Cubone's DPS by 150%");
    private static final Upgrade u5 = new Upgrade(5, 100, new UpgradeData(0, 0, 0.005), 2, 5e5, "Increases Click Damage by 0.5% of total DPS");

    public Cubone() {
        super(2, "Cubone", 50, 5, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}