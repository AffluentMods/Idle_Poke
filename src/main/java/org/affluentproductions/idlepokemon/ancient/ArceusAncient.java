package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.entity.Player;

import java.math.BigInteger;

public class ArceusAncient extends Ancient {

    public ArceusAncient() {
        super(7, "Arceus", -1, "Every upgrade adds 11% Base DPS");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return BigInteger.ONE;
    }

    @Override
    public void summon(Player player, int level) {
        player.addDPSMultiplier(getName() + " Ancient", level * 0.11);
    }

    @Override
    public void reactivate(Player player, int level) {
        player.addDPSMultiplier(getName() + " Ancient", level * 0.11);
    }
}