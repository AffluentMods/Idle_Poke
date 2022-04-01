package org.affluentproductions.idlepokemon.entity.pokemon.sub1;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;

public class Pichu extends Pokemon {

    private static final Upgrade u1 = new Upgrade(1, 10, new UpgradeData(0, 1), 1, 100, "Increases Pichu's Click Damage by 100%");
    private static final Upgrade u2 = new Upgrade(2, 25, new UpgradeData(Skill.getSkill("Extreme Speed")), 1, 250, "Unlocks Extreme Speed Skill");
    private static final Upgrade u3 = new Upgrade(3, 50, new UpgradeData(0, 1), 1, 1000, "Increases Pichu's Click Damage by 100%");
    private static final Upgrade u4 = new Upgrade(4, 75, new UpgradeData(0, 1), 1, 8000, "Increases Pichu's Click Damage by 100%");
    private static final Upgrade u5 = new Upgrade(5, 100, new UpgradeData(0, 1.5), 1, 80000, "Increases Pichu's Click Damage by 150%");
    private static final Upgrade u6 = new Upgrade(6, 125, new UpgradeData(0, 2), 1, 4e5, "Increases Pichu's Click Damage by 200%");
    private static final Upgrade u7 = new Upgrade(7, 150, new UpgradeData(0, 2.5), 1, 4e6, "Increases Pichu's Click Damage by 250%");

    public Pichu() {
        super(1, "Pichu", 5, 1, 1);
        addUpgrade(u1, u2, u3, u4, u5, u6, u7);
    }
}