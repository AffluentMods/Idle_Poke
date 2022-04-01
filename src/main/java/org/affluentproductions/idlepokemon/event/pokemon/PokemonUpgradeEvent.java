package org.affluentproductions.idlepokemon.event.pokemon;

import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;

public class PokemonUpgradeEvent extends PokemonEvent {

    private final Upgrade upgrade;

    public PokemonUpgradeEvent(String userId, Pokemon pokemon, Upgrade upgrade) {
        super(userId, pokemon);
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }
}