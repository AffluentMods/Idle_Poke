package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.IdlePokemon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CooldownUtil {

    private static final HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<>();

    public static void addCooldown(String uid, String name, long until, boolean sql) {
        HashMap<String, Long> userCooldowns = cooldowns.getOrDefault(uid, new HashMap<>());
        if (userCooldowns.containsKey(name.toLowerCase())) {
            removeCooldown(uid, name);
            addCooldown(uid, name, until, sql);
            return;
        }
        userCooldowns.put(name.toLowerCase(), until);
        cooldowns.put(uid, userCooldowns);
        if (sql) IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO cooldowns VALUES (?, ?, ?);", uid, name.toLowerCase(), String.valueOf(until));
    }

    public static void removeCooldown(String uid, String name) {
        HashMap<String, Long> userCooldowns = cooldowns.getOrDefault(uid, new HashMap<>());
        userCooldowns.remove(name.toLowerCase());
        cooldowns.put(uid, userCooldowns);
        IdlePokemon.getBot().getDatabase()
                .update("DELETE FROM cooldowns WHERE userId=? AND cooldownName=?;", uid, name.toLowerCase());
    }

    public static long getCooldown(String uid, String name) {
        HashMap<String, Long> userCooldowns = cooldowns.getOrDefault(uid, new HashMap<>());
        return userCooldowns.getOrDefault(name, -1L);
    }

    public static String format(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = 0;
        long hours = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        String hoursString = String.valueOf(hours);
        String minutesString = String.valueOf(minutes);
        String secondsString = String.valueOf(seconds);
        if (hoursString.length() == 1) hoursString = "0" + hoursString;
        if (minutesString.length() == 1) minutesString = "0" + minutesString;
        if (secondsString.length() == 1) secondsString = "0" + secondsString;
        return hoursString + ":" + minutesString + ":" + secondsString;
    }

    public static String format(long milliseconds, String uid) {
        long seconds = milliseconds / 1000;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        while (hours >= 24) {
            hours -= 24;
            days++;
        }
        String format = "";
        if (days > 0) format += days + " days, ";
        if (hours > 0) format += hours + " hours, ";
        if (minutes > 0) format += minutes + " minutes, ";
        format += seconds + " seconds";
        return format;
    }

    public static void loadCooldowns() {
        cooldowns.clear();
        long now = System.currentTimeMillis();
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM cooldowns;")) {
            while (rs.next()) {
                String uid = rs.getString("userId");
                String cdName = rs.getString("cooldownName");
                String cdEnd = rs.getString("cooldownEnd");
                long cdEndLong = Long.parseLong(cdEnd);
                if (cdEndLong <= now) {
                    IdlePokemon.getBot().getDatabase()
                            .update("DELETE FROM cooldowns WHERE userId=? AND cooldownName=? AND cooldownEnd=?;", uid,
                                    cdName, cdEnd);
                    continue;
                }
                addCooldown(uid, cdName, cdEndLong, false);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}