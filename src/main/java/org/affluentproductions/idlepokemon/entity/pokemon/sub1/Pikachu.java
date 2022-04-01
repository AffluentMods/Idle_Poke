package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Pikachu extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 8, 4e6, "Increases Pikachu's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 8, 1e7, "Increases Pikachu's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 8, 4e7, "Increases Pikachu's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(0.25, 0), -1, 3.2e8, "Increases total DPS by 25%");

    // Leon
    public Pikachu() {
        super(8, "Pikachu", 400000, 10859, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}