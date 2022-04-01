package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Spiritomb extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1.25, 0), 18, 3.2e15, "Increases Spiritomb's DPS by 125%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1.25, 0), 18, 8e15, "Increases Spiritomb's DPS by 125%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1.25, 0), 18, 3.2e16, "Increases Spiritomb's DPS by 125%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(Skill.getSkill("Clangorous Soul")), 18, 2.56e17,
                    "Unlocks Clangorous Soul Skill");

    // Abaddon
    public Spiritomb() {
        super(18, "Spiritomb", new BigDecimal(Double.toString(3.200e14)).toBigInteger(), 1.310e11, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}