package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Slaking extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(0.25, 0), -1, 4.2e25, "Increases total DPS by 25%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 25, 1.05e26, "Increases Slaking's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(0.25, 0), -1, 4.2e26, "Increases total DPS by 25%");
    private static final Upgrade u4 =
            new Upgrade(4, 100, new UpgradeData(1, 0), 25, 3.36e27, "Increases Slaking's DPS by 100%");

    // 25 Slaking = Grant
    public Slaking() {
        super(25, "Slaking", new BigDecimal(Double.toString(4.200e24)).toBigInteger(), 2.027e20, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}