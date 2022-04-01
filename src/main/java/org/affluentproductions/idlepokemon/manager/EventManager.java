package org.affluentproductions.idlepokemon.manager;

import org.affluentproductions.idlepokemon.event.Event;
import org.affluentproductions.idlepokemon.event.action.ClickEvent;
import org.affluentproductions.idlepokemon.event.economy.EconomyEvent;
import org.affluentproductions.idlepokemon.event.pokemon.PokemonLevelEvent;
import org.affluentproductions.idlepokemon.event.pokemon.PokemonUpgradeEvent;
import org.affluentproductions.idlepokemon.listener.EventListener;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static List<EventListener> listeners = new ArrayList<>();

    public static void callEvent(Event event) {
        if (event instanceof EconomyEvent)
            for (EventListener eventListener : listeners) eventListener.onEconomyEvent((EconomyEvent) event);
        if (event instanceof PokemonLevelEvent)
            for (EventListener eventListener : listeners) eventListener.onPokemonLevelEvent((PokemonLevelEvent) event);
        if (event instanceof PokemonUpgradeEvent) for (EventListener eventListener : listeners)
            eventListener.onPokemonUpgradeEvent((PokemonUpgradeEvent) event);
        if (event instanceof ClickEvent)
            for (EventListener eventListener : listeners) eventListener.onClickEvent((ClickEvent) event);
    }

    public static void addListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

}