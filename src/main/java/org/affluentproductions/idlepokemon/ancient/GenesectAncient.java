package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.entity.Player;

import java.math.BigInteger;

public class GenesectAncient extends Ancient {

    public GenesectAncient() {
        super(9, "Genesect", -1, "Every upgrade adds 2% Shiny DPS");
    }

    @Override
    public BigInteger getCost(int toLevel) {
        return BigInteger.valueOf(toLevel);
    }

    @Override
    public void summon(Player player, int level) {
        player.addShinyDPSMultiplier(getName() + " Ancient", level * 0.02);
    }

    @Override
    public void reactivate(Player player, int level) {
        player.addShinyDPSMultiplier(getName() + " Ancient", level * 0.02);
    }
}