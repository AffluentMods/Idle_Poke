package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.CooldownUtil;

public class HelpingHand extends Skill {

    public HelpingHand() {
        super(8, "Helping Hand", "Doubles the effect of the next skill you use. (Useable within 1 hour)",
                new SkillEffect(-1) {
                    @Override
                    public boolean canDoubleEffect() {
                        return false;
                    }

                    @Override
                    public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                        long cd = System.currentTimeMillis() + (60 * 60 * 1000);
                        CooldownUtil.addCooldown(player.getUserId(), "skill_double", cd, true);
                    }

                    @Override
                    public void deactivate(Player player) {
                    }
                }, 23, 4, 100, 60 * 60 * 1000);
    }

}