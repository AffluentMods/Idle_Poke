package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Kingdra extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 28, 1e56, "Increases Kingdra's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 28, 2.5e56, "Increases Kingdra's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 28, 1e57, "Increases Kingdra's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1.5, 0), 28, 8e57, "Increases Kingdra's DPS by 150%");

    // 28 Kingdra = Atlas
    public Kingdra() {
        super(28, "Kingdra", new BigDecimal(Double.toString(1.000e55)).toBigInteger(), 9.655e44, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}