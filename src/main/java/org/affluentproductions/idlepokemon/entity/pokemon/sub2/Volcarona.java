package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Volcarona extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 21, 3e18, "Increases Volcarona's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(1, 0), 21, 7.5e18, "Increases Volcarona's DPS by 100%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(1, 0), 21, 3e19, "Increases Volcarona's DPS by 100%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(0.1, 0), -1, 2.4e20, "Increases total DPS by 10%");
    private static final Upgrade u5 = new Upgrade(5, 100, new UpgradeData(Skill.getSkill("Close Combat")), 21, 2.4e21,
            "Unlocks Close Combat Skill");

    // 21 Volcarona = Beastlord
    public Volcarona() {
        super(21, "Volcarona", new BigDecimal(Double.toString(3.000e17)).toBigInteger(), 4.914e13, 0);
        addUpgrade(u1, u2, u3, u4, u5);
    }
}