package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Mienshao extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 27, 1e41, "Increases Mienshao's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 27, 2.5e41, "Increases Mienshao's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 27, 1e42, "Increases Mienshao's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 100, new UpgradeData(1.5, 0), 27, 8e42, "Increases Mienshao's DPS by 150%");

    // 27 Mienshao = Dread Knight
    public Mienshao() {
        super(27, "Mienshao", new BigDecimal(Double.toString(1.000e40)).toBigInteger(), 1.310e32, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}