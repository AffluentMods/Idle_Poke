package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Ludicolo extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 23, 3.5e21, "Increases Ludicolo's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 23, 8.75e21, "Increases Ludicolo's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 23, 3.5e22, "Increases Ludicolo's DPS by 100%");
    private static final Upgrade u4 = new Upgrade(4, 100, new UpgradeData(Skill.getSkill("Helping Hand")), 23, 2.8e23,
            "Unlocks Helping Hand Skill");
    private static final Upgrade u5 =
            new Upgrade(5, 125, new UpgradeData(1, 0), 23, 2.8e24, "Increases Ludicolo's DPS by 100%");

    // 23 Ludicolo = Aphrodite
    public Ludicolo() {
        super(23, "Ludicolo", new BigDecimal(Double.toString(3.500e20)).toBigInteger(), 3.112e16, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}