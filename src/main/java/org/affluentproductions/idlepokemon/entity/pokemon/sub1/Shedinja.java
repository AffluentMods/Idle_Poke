package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Shedinja extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData("3"), 10, 1.5e8, "Increases Critical Chance by 3%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1.25, 0.0), 10, 3.75e8, "Increases Shedinja's DPS by 125%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1.25, 0.0), 10, 1.5e9, "Increases Shedinja's DPS by 125%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData("0", "5"), 10, 1.2e10, "Increases Critical Multiplier by x5");
    private static final Upgrade u5 = new Upgrade(5, 100, new UpgradeData(Skill.getSkill("Focus Energy")), 10, 1.2e11,
            "Unlocks Focus Energy Skill");

    // Alexa
    public Shedinja() {
        super(10, "Shedinja", 15000000L, 186900, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}