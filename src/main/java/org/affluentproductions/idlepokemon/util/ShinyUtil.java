package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ShinyUtil {
    public static void load() {
        IdlePokemon.getBot().getDatabase()
                .ct("shinies", "userId VARCHAR(24) NOT NULL, pid INT NOT NULL, amount INT NOT NULL");
        IdlePokemon.getBot().getDatabase().ct("shiny_util", "userId VARCHAR(24) NOT NULL, lastStage INT NOT NULL");
    }

    private static final HashMap<String, HashMap<Integer, Integer>> cache = new HashMap<>();

    public static boolean receiveShiny(String userId, int stage) {
        boolean a = false;
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM shiny_util WHERE userId=?;", userId)) {
            int lastStage = 0;
            if (rs.next()) lastStage = rs.getInt("lastStage");
            if (stage % 100 == 0) {
                if (stage > lastStage) a = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return a;
    }

    public static void setLastStage(String userId, int lastStage) {
        IdlePokemon.getBot().getDatabase().update("DELETE FROM shiny_util WHERE userId=?;", userId);
        IdlePokemon.getBot().getDatabase().update("INSERT INTO shiny_util VALUES (?, ?);", userId, lastStage);
    }

    public static int getShinies(String userId, Pokemon pokemon) {
        HashMap<Integer, Integer> s = cache.getOrDefault(userId, new HashMap<>());
        if (s.containsKey(pokemon.getID())) return s.get(pokemon.getID());
        int shinies = 0;
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM shinies WHERE userId=? AND pid=?;", userId, pokemon.getID())) {
            if (rs.next()) shinies = rs.getInt("amount");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        s.put(pokemon.getID(), shinies);
        cache.put(userId, s);
        return shinies;
    }

    public static void addShiny(Player p, Pokemon pokemon) {
        String userId = p.getUserId();
        HashMap<Integer, Integer> s = cache.getOrDefault(userId, new HashMap<>());
        int shinies = getShinies(userId, pokemon);
        shinies++;
        s.put(pokemon.getID(), shinies);
        cache.put(userId, s);
        p.updateDPS();
        p.updateCD();
        IdlePokemon.getBot().getDatabase()
                .update("DELETE FROM shinies WHERE userId=? AND pid=?;", userId, pokemon.getID());
        IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO shinies VALUES (?, ?, ?);", userId, pokemon.getID(), shinies);
    }

    public static void removeShiny(Player p, Pokemon pokemon) {
        String userId = p.getUserId();
        HashMap<Integer, Integer> s = cache.getOrDefault(userId, new HashMap<>());
        int shinies = getShinies(userId, pokemon);
        shinies--;
        if (shinies < 0) shinies = 0;
        s.put(pokemon.getID(), shinies);
        cache.put(userId, s);
        p.updateDPS();
        p.updateCD();
        IdlePokemon.getBot().getDatabase()
                .update("DELETE FROM shinies WHERE userId=? AND pid=?;", userId, pokemon.getID());
        if (shinies > 0) IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO shinies VALUES (?, ?, ?);", userId, pokemon.getID(), shinies);
    }
}