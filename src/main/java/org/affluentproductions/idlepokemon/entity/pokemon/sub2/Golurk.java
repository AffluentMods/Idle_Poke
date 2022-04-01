package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Golurk extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 19, 2.7e16, "Increases Golurk's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 19, 6.75e16, "Increases Golurk's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 19, 2.7e17, "Increases Golurk's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1.5, 0), 19, 2.16e18, "Increases Golurk's DPS by 150%");

    // 19 Golurk = Ma Zhu
    public Golurk() {
        super(19, "Golurk", new BigDecimal(Double.toString(2.700e15)).toBigInteger(), 8.147e11, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}