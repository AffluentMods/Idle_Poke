package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.skill.Skill;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RayquazaAncient extends Ancient {

    public RayquazaAncient() {
        super(6, "Rayquaza", 1785, "Every upgrade adds 2 seconds to Close Combat Skill");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return new BigDecimal("2").pow(toLevel).toBigInteger();
    }

    @Override
    public void summon(Player player, int level) {
        BonusType sd = BonusType.getSkillDuration(Skill.getSkill("close combat"));
        player.addBonus(getName() + " Ancient", new Bonus(level * 2, sd));
    }

    @Override
    public void reactivate(Player player, int level) {
        BonusType sd = BonusType.getSkillDuration(Skill.getSkill("close combat"));
        player.addBonus(getName() + " Ancient", new Bonus(level * 2, sd));
    }
}