package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Jigglypuff extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 3, 2500, "Increases Jigglypuff's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 3, 6250, "Increases Jigglypuff's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 3, 25000, "Increases Jigglypuff's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(Skill.getSkill("Sword Dance")), 3, 2e5, "Unlocks Sword Dance Skill");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData(0, 0, 0.005), 3, 2e6, "Increases Click Damage by 0.5% of total DPS");
    private static final Upgrade u6 =
            new Upgrade(6, 125, new UpgradeData(1.5, 0), 3, 1e7, "Increases Jigglypuff's DPS by 150%");

    public Jigglypuff() {
        super(3, "Jigglypuff", 250, 22, 0);
        addUpgrade(u1, u2, u3, u4, u5, u6);
    }
}