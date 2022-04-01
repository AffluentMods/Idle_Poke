package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.entity.Player;

public class SwordDance extends Skill {

    public SwordDance() {
        super(2, "Sword Dance", "+100% DPS for 30 seconds", new SkillEffect(30 * 1000) {
            @Override
            public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                SwordDance.run(player, isBuy, doubleEffect);
            }

            @Override
            public void deactivate(Player player) {
                SwordDance.end(player);
            }
        }, 3, 4, 75, 10 * 60 * 1000);
    }

    public static void run(Player player, boolean isBuy, boolean doubleEffect) {
        double val = 1;
        if (doubleEffect) val = val * 2;
        player.addDPSMultiplier("Sword Dance", val);
        player.updateDPS();
        player.updateCD();
    }

    public static void end(Player player) {
        Player p = new Player(player.getUserId());
        p.removeDPSMultiplier("Sword Dance");
        p.updateDPS();
        p.updateCD();
    }
}