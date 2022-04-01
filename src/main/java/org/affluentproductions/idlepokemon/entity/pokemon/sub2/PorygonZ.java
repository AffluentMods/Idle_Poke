package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class PorygonZ extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 22, 9e19, "Increases Porygon-Z's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 22, 2.25e20, "Increases Porygon-Z's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 22, 9e20, "Increases Porygon-Z's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1, 0), 22, 7.2e21, "Increases Porygon-Z's DPS by 100%");

    // 22 Porygon-Z = Athena
    public PorygonZ() {
        super(22, "Porygon-Z", new BigDecimal(Double.toString(9.000e18)).toBigInteger(), 1.086e15, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}