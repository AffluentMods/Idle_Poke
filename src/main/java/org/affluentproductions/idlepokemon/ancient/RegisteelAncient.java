package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.skill.Skill;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RegisteelAncient extends Ancient {

    public RegisteelAncient() {
        super(3, "Registeel", 885, "Every upgrade adds 2 seconds to Focus Energy Skill");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return new BigDecimal("2").pow(toLevel).toBigInteger();
    }

    @Override
    public void summon(Player player, int level) {
        BonusType sd = BonusType.getSkillDuration(Skill.getSkill("focus energy"));
        player.addBonus(getName() + " Ancient", new Bonus(level * 2, sd));
    }

    @Override
    public void reactivate(Player player, int level) {
        BonusType sd = BonusType.getSkillDuration(Skill.getSkill("focus energy"));
        player.addBonus(getName() + " Ancient", new Bonus(level * 2, sd));
    }
}