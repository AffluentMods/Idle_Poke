package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Zoroark extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 29, 1e72, "Increases Zoroark's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 29, 2.5e71, "Increases Zoroark's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 29, 1e72, "Increases Zoroark's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 100, new UpgradeData(1.5, 0), 29, 8e72, "Increases Zoroark's DPS by 150%");

    // 29 Zoroark = Terra
    public Zoroark() {
        super(29, "Zoroark", new BigDecimal(Double.toString(1.000e70)).toBigInteger(), 7.113e57, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}