package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Crobat extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(0.25, 0), -1, 5e11, "Increases total DPS by 25%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0.0), 14, 1.25e12, "Increases Crobat's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0.0), 14, 5e12, "Increases Crobat's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(1, 0), 14, 4e13, "Increases Crobat's DPS by 100%");
    private static final Upgrade u5 = new Upgrade(5, 100, new UpgradeData(Skill.getSkill("Happy Hour")), 14, 4e14,
            "Unlocks Happy Hour Skill");

    public Crobat() {
        super(14, "Crobat", 50000000000L, 69480000, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}