package org.affluentproductions.idlepokemon.entity;

import org.affluentproductions.idlepokemon.IdlePokemon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PokemonUser {

    private static final HashMap<String, PokemonUser> cache = new HashMap<>();

    private final String userId;
    private HashMap<Integer, Pokemon> pokemons;

    public PokemonUser(String userId) {
        this.userId = userId;
        this.pokemons = new HashMap<>();
        load();
        cache();
    }

    private void loadCache(PokemonUser cached) {
        this.pokemons = cached.getPokemons();
    }

    private void load() {
        if (cache.containsKey(userId)) {
            loadCache(cache.get(userId));
            return;
        }
        pokemons = new HashMap<>();
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM pokemons WHERE userId=?;", userId)) {
            while (rs.next()) {
                int pid = rs.getInt("pid");
                int level = rs.getInt("level");
                Pokemon pokemon = new Pokemon(pid, level);
                pokemons.put(pid, pokemon);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        cache();
    }

    public Pokemon getPokemon(int ID) {
        HashMap<Integer, Pokemon> pokemons = getPokemons();
        if (!pokemons.containsKey(ID)) return null;
        Pokemon pokemon = pokemons.get(ID);
        int lv = pokemon == null ? 0 : pokemon.getLevel();
        return new Pokemon(pokemon.getID(), lv);
    }

    public Pokemon getPokemonByName(String name) {
        for (Pokemon pokemon : pokemons.values()) if (pokemon.getName().equalsIgnoreCase(name)) return pokemon;
        return null;
    }

    public void removePokemon(int pid) {
        pokemons.remove(pid);
        cache();
        IdlePokemon.getBot().getDatabase().update("DELETE FROM pokemons WHERE userId=? AND pid=?;", userId, pid);
    }

    public void setPokemon(Pokemon newPokemon) {
        int pid = newPokemon.getID();
        int lv = newPokemon.getLevel();
        if (lv > 0) pokemons.put(pid, newPokemon);
        else pokemons.remove(pid);
        cache();
        IdlePokemon.getBot().getDatabase().update("DELETE FROM pokemons WHERE userId=? AND pid=?;", userId, pid);
        if (lv > 0) IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO pokemons (userId, pid, level) VALUES (?, ?, ?);", userId, newPokemon.getID(),
                        newPokemon.getLevel());
    }

    public HashMap<Integer, Pokemon> getPokemons() {
        return pokemons;
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static void clearCache() {
        cache.clear();
    }
}