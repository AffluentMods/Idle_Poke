package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.IdlePokemon;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsUtil {

    public static void setStat(String userId, String stat_name, String stat_value) {
        IdlePokemon.getBot().getDatabase()
                .update("DELETE FROM stats WHERE userId=? AND statistic_name=?;", userId, stat_name);
        IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO stats (userId, statistic_name, statistic_value) VALUES (?, ?, ?);", userId,
                        stat_name, stat_value);
    }

    public static String getStat(String userId, String stat_name, String default_value) {
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT statistic_value FROM stats WHERE userId=? AND statistic_name=?;", userId, stat_name)) {
            if (rs.next()) {
                return rs.getString("statistic_value");
            }
            return default_value;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return default_value;
        }
    }
}