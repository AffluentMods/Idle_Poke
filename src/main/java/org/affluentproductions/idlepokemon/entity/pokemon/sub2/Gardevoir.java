package org.affluentproductions.idlepokemon.entity.pokemon.sub2;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

import java.math.BigDecimal;

public class Gardevoir extends Pokemon {
    private static final Upgrade u1 =
            new Upgrade(1, 10, new UpgradeData(1, 0), 20, 2.4e17, "Increases Gardevoir's DPS by 100%");
    private static final Upgrade u2 =
            new Upgrade(2, 25, new UpgradeData(0.2, 0), -1, 6e17, "Increases total DPS by 20%");
    private static final Upgrade u3 =
            new Upgrade(3, 50, new UpgradeData(0.2, 0), -1, 2.4e18, "Increases total DPS by 20%");
    private static final Upgrade u4 =
            new Upgrade(4, 75, new UpgradeData(Skill.getSkill("Evolution")), 20, 1.92e19, "Unlocks Evolution");

    // 20 Gardevoir = Amenhotep
    public Gardevoir() {
        super(20, "Gardevoir", new BigDecimal(Double.toString(2.400e16)).toBigInteger(), 5.335e12, 0);
        addUpgrade(u1, u2, u3, u4);
    }
}