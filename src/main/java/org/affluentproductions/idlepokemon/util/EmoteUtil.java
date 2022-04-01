package org.affluentproductions.idlepokemon.util;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EmoteUtil {

    private static final HashMap<String, String> cache = new HashMap<>();
    private static List<Guild> officialGuilds = null;

    private static void loadEmoteGuilds() {
        if (officialGuilds == null) {
            ShardManager shardManager = IdlePokemon.getBot().getShardManager();
            officialGuilds = new ArrayList<>();
            for (String emoteGuild : Constants.emoteGuilds)
                officialGuilds.add(shardManager.getGuildById(emoteGuild));
            officialGuilds.add(shardManager.getGuildById(Constants.main_guild));
        }
    }

    public static String getEmoteMention(String name, List<String> replaceAll, List<String> with) {
        loadEmoteGuilds();
        if (replaceAll != null && with != null && replaceAll.size() > 0 && with.size() > 0) {
            if (replaceAll.size() != with.size()) {
                System.out.println("[INTERN ERR] Error: replaceAll size != with size | getEmoteMention(...)");
                return "[ERR]";
            }
            for (int i = 0; i < replaceAll.size(); i++) {
                String arg = replaceAll.get(i);
                String argWith = with.get(i);
                name = name.replaceAll(arg, argWith);
            }
        }
        if (cache.containsKey(name)) return cache.get(name);
        Emote emote = null;
        for (Guild guild : officialGuilds) {
            if (guild == null) continue;
            List<Emote> emotes = guild.getEmotesByName(name, true);
            if (emotes.size() > 0) {
                emote = emotes.get(0);
                break;
            }
        }
        if (emote == null) {
            return "";
        }
        cache.put(name.replaceAll(" ", "_"), emote.getAsMention());
        return emote.getAsMention();
    }

    private static Emote getEmote(String name) {
        loadEmoteGuilds();
        for (Guild guild : officialGuilds) {
            if (guild == null) continue;
            List<Emote> emotes = guild.getEmotesByName(name, true);
            if (emotes.size() > 0) {
                return emotes.get(0);
            }
        }
        return null;
    }

    public static String getEmoteMentionOrDefaultAsString(String name, String defaultName) {
        Emote emote = getEmote(name);
        if (emote == null) return defaultName;
        return emote.getAsMention();
    }

    public static String getEmoteMention(String name) {
        return getEmoteMention(name, Arrays.asList(" ", "-"), Arrays.asList("_", "_"));
    }

    public static String getSoul() {
        return getEmoteMention("Pokemon_Soul");
    }

    public static String getRuby() {
        return getEmoteMention("Ruby");
    }

    public static String getCoin() {
        return getEmoteMention("Pokecoin");
    }

    public static void clearCache() {
        cache.clear();
    }
}