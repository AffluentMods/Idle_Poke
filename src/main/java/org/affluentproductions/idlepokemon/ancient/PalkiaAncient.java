package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.skill.Skill;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PalkiaAncient extends Ancient {

    public PalkiaAncient() {
        super(2, "Palkia", 285, "Every upgrade adds 2 seconds to Sword Dance Skill");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return new BigDecimal("2").pow(toLevel).toBigInteger();
    }

    @Override
    public void summon(Player player, int level) {
        BonusType sd = BonusType.getSkillDuration(Skill.getSkill("sword dance"));
        player.addBonus(getName() + " Ancient", new Bonus(level * 2, sd));
    }

    @Override
    public void reactivate(Player player, int level) {
        BonusType sd = BonusType.getSkillDuration(Skill.getSkill("sword dance"));
        player.addBonus(getName() + " Ancient", new Bonus(level * 2, sd));
    }
}