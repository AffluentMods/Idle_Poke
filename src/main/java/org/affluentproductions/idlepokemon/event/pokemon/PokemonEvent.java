package org.affluentproductions.idlepokemon.event.pokemon;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.event.Event;

public class PokemonEvent extends Event {

    private final Pokemon pokemon;

    public PokemonEvent(String userId, Pokemon pokemon) {
        super(userId);
        this.pokemon = pokemon;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }
}