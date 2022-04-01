package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.skill.PayDay;

import java.math.BigInteger;

public class DreepyAncient extends Ancient {

    public DreepyAncient() {
        super(11, "Dreepy", -1, "Every upgrade adds 30% more gold drop from Pay Day Skill");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return BigInteger.valueOf(toLevel);
    }

    @Override
    public void summon(Player player, int level) {
        PayDay.multiplier.put(player.getUserId(), level * 0.05);
    }

    @Override
    public void reactivate(Player player, int level) {
        PayDay.multiplier.put(player.getUserId(), level * 0.05);
    }
}