package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.entity.Player;

public abstract class SkillEffect {

    private final long activeTime;

    public SkillEffect(long activeTime) {
        this.activeTime = activeTime;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public abstract void activate(Player player, boolean isBuy, boolean doubleEffect);

    public abstract void deactivate(Player player);

    public void reactivate(Player player, int amount) {
    }

    public boolean canDoubleEffect() {
        return true;
    }

    public boolean isProduct() {
        return false;
    }
}