package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

import java.math.BigInteger;

public class HeatranAncient extends Ancient {

    public HeatranAncient() {
        super(8, "Heatran", -1, "Every upgrade adds 20% Click Damage");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return BigInteger.valueOf(toLevel);
    }

    @Override
    public void summon(Player player, int level) {
        player.addBonus(getName() + " Ancient", new Bonus(level * 0.2, BonusType.CLICK_DAMAGE));
    }

    @Override
    public void reactivate(Player player, int level) {
        player.addBonus(getName() + " Ancient", new Bonus(level * 0.2, BonusType.CLICK_DAMAGE));
    }
}