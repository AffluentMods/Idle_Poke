package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.CooldownUtil;

public class Replenish extends Skill {

    public Replenish() {
        super(9, "Replenish", "Lowers cooldown of last skill used by 1 hour.", new SkillEffect(-1) {
            @Override
            public boolean canDoubleEffect() {
                return false;
            }

            @Override
            public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                Skill lastSkillUsed = getLastSkillUsed(player);
                if (lastSkillUsed == null) return;
                long now = System.currentTimeMillis();
                String uid = player.getUserId();
                long till = CooldownUtil.getCooldown(uid, "skill_cd_" + lastSkillUsed.getID());
                if (till != -1) {
                    long diff = till - now;
                    if (diff > 0) {
                        long newTill = till - 60 * 60 * 1000;
                        CooldownUtil.addCooldown(uid, "skill_cd_" + lastSkillUsed.getID(), newTill, true);
                    }
                }
            }

            @Override
            public void deactivate(Player player) {
            }
        }, 24, 5, 100, 60 * 60 * 1000);
    }

    private static Skill getLastSkillUsed(Player player) {
        Skill skill = null;
        String uid = player.getUserId();
        long lastPassed = -1;
        long now = System.currentTimeMillis();
        for (Skill s : Skill.getSkills().values()) {
            int sid = s.getID();
            if (sid == 8 || sid == 9) continue;
            long scd = s.getCooldown();
            long pscd = CooldownUtil.getCooldown(uid, "skill_cd_" + sid);
            if (pscd == -1) continue;
            long diff = pscd - now;
            if (diff > 0) {
                long passed = (scd - (pscd - now));
                if (lastPassed == -1) {
                    lastPassed = passed;
                    skill = s;
                } else {
                    if (lastPassed > passed) {
                        lastPassed = passed;
                        skill = s;
                    }
                }
            }
        }
        return skill;
    }
}