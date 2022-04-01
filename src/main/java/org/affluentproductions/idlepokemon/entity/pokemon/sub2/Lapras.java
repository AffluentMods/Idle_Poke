package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Lapras extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 26, 2.1e28, "Increases Lapras's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 26, 5.25e28, "Increases Lapras's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(0.25, 0), -1, 2.1e29, "Increases total DPS by 25%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(0, 0, 0.005), 26, 1.68e30, "Increases Lapras's DPS by 100%");

    // 26 Lapras = Frostleaf
    public Lapras() {
        super(26, "Lapras", new BigDecimal(Double.toString(2.100e27)).toBigInteger(), 7.469e22, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}