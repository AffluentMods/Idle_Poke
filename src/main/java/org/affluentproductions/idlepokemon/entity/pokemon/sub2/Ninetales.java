package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Ninetales extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 24, 1.4e23, "Increases Ninetales's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(0.1, 0), -1, 3.5e23, "Increases total DPS by 10%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 24, 1.4e24, "Increases Ninetales's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1, 0), 24, 1.12e25, "Increases Ninetales's DPS by 100%");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData(Skill.getSkill("Replenish")), 24, 1.12e26, "Unlocks Replenish Skill");

    // 24 Ninetales = Shinatobe
    public Ninetales() {
        super(24, "Ninetales", new BigDecimal(Double.toString(1.400e22)).toBigInteger(), 9.173e18, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}