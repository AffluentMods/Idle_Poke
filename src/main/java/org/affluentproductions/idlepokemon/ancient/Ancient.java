package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.EmoteUtil;

import java.math.BigInteger;

public abstract class Ancient {

    private final int ID;
    private final String name;
    private final String description;
    private final int maxLevel;

    public Ancient(int ID, String name, int maxLevel, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
    }

    public int getID() {
        return ID;
    }

    public String getDisplay() {
        return EmoteUtil.getEmoteMention(name) + " " + getName();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public abstract BigInteger getCost(int toLevel);

    public abstract void summon(Player player, int level);

    public abstract void reactivate(Player player, int level);
}