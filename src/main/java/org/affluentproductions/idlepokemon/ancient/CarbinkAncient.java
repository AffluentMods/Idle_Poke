package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

import java.math.BigInteger;

public class CarbinkAncient extends Ancient {

    public CarbinkAncient() {
        super(10, "Carbink", -1, "Every upgrade adds 5% Gold Drop");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return BigInteger.valueOf(toLevel);
    }

    @Override
    public void summon(Player player, int level) {
        player.addBonus(getName() + " Ancient", new Bonus(level * 0.05, BonusType.GOLD_DROP));
    }

    @Override
    public void reactivate(Player player, int level) {
        player.addBonus(getName() + " Ancient", new Bonus(level * 0.05, BonusType.GOLD_DROP));
    }
}