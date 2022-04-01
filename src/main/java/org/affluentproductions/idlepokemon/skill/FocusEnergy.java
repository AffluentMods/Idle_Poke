package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

public class FocusEnergy extends Skill {

    public FocusEnergy() {
        super(3, "Focus Energy", "+50% chance for critical hit for 30 seconds", new SkillEffect(30 * 1000) {
            @Override
            public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                FocusEnergy.run(player, isBuy, doubleEffect);
            }

            @Override
            public void deactivate(Player player) {
                FocusEnergy.end(player);
            }
        }, 10, 5, 100, 30 * 60 * 1000);
    }

    public static void run(Player player, boolean isBuy, boolean doubleEffect) {
        double val = 0.5;
        if (doubleEffect) val = val * 2;
        player.addBonus("Focus Energy", new Bonus(val, BonusType.CRITICAL_HIT));
    }

    public static void end(Player player) {
        new Player(player.getUserId()).removeBonus("Focus Energy");
    }
}