package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Vaporeon extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(new Bonus(0.25, BonusType.GOLD_DROP)), 16, 4e13,
                    "Increases Coin Gain by 25%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(new Bonus(0.25, BonusType.GOLD_DROP)), 16, 1e14,
                    "Increases Coin Gain by 25%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(new Bonus(0.25, BonusType.GOLD_DROP)), 16, 4e14,
                    "Increases Coin Gain by 25%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(new Bonus(0.5, BonusType.GOLD_DROP)), 16, 3.2e15,
                    "Increases Coin Gain by 50%");
    private static final Upgrade u5 =
            new Upgrade(5, 100, new UpgradeData(Skill.getSkill("Pay Day")), 16, 3.2e16, "Unlocks Pay Day Skill");
    private static final Upgrade u6 =
            new Upgrade(6, 125, new UpgradeData("3"), 16, 1.6e17, "Increases Critical Chance by 3%");

    // King Midas
    public Vaporeon() {
        super(16, "Vaporeon", new BigDecimal(Double.toString(4.000e12)).toBigInteger(), 3.017e9, 0);
        addUpgrade(u1, u2, u3, u4, u5, u6);
    }
}