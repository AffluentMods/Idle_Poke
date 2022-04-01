package org.affluentproductions.idlepokemon.event.pokemon;

import org.affluentproductions.idlepokemon.entity.Pokemon;

public class PokemonLevelEvent extends PokemonEvent {

    private final int oldLevel;
    private final int newLevel;

    public PokemonLevelEvent(String userId, Pokemon pokemon, int oldLevel, int newLevel) {
        super(userId, pokemon);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public int getOldLevel() {
        return oldLevel;
    }
}