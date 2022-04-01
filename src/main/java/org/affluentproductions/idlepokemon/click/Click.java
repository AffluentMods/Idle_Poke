package org.affluentproductions.idlepokemon.click;

import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Rival;

public class Click {

    private final Player p;
    private final Rival rival;
    private final Rival newRival;
    private double damage;

    public Click(Player p, Rival rival, Rival newRival, double damage) {
        this.p = p;
        this.rival = rival;
        this.newRival = newRival;
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public Player getPlayer() {
        return p;
    }

    public Rival getRival() {
        return rival;
    }

    public Rival getNewRival() {
        return newRival;
    }

    public boolean isKill() {
        return rival.getHealth() <= 0;
    }
}