package org.affluentproductions.idlepokemon.listener;

import org.affluentproductions.idlepokemon.event.action.ClickEvent;
import org.affluentproductions.idlepokemon.event.economy.EconomyEvent;
import org.affluentproductions.idlepokemon.event.pokemon.PokemonLevelEvent;
import org.affluentproductions.idlepokemon.event.pokemon.PokemonUpgradeEvent;

public interface EventListener {
    default void onPokemonLevelEvent(PokemonLevelEvent e) {}

    default void onEconomyEvent(EconomyEvent e) {}

    default void onPokemonUpgradeEvent(PokemonUpgradeEvent e) {}

    default void onClickEvent(ClickEvent e) {}
}
