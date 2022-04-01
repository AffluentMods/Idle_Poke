package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

public class HappyHour extends Skill {

    public HappyHour() {
        super(4, "Happy Hour", "+100% Gold dropped for 30 seconds", new SkillEffect(30 * 1000) {
            @Override
            public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                HappyHour.run(player, isBuy, doubleEffect);
            }

            @Override
            public void deactivate(Player player) {
                HappyHour.end(player);
            }
        }, 14, 5, 100, 30 * 60 * 1000);
    }

    public static void run(Player player, boolean isBuy, boolean doubleEffect) {
        double val = 1.0;
        if (doubleEffect) val = val * 2;
        player.addBonus("Happy Hour", new Bonus(val, BonusType.GOLD_DROP));
    }

    public static void end(Player player) {
        player.removeBonus("Happy Hour");
    }
}