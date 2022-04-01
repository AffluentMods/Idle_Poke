package org.affluentproductions.idlepokemon.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ServerUtil {

    private static final HashMap<Long, Long> updateChannels = new HashMap<>();

    static void load() {
        try {
            final ResultSet rs =
                    IdlePokemon.getBot().getDatabase().query("SELECT guildId, channelId FROM offServerUpdates;");
            while (rs.next()) {
                updateChannels.put(Long.parseLong(rs.getString("guildId")), Long.parseLong(rs.getString("channelId")));
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try (final ResultSet urs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM updatePosts;")) {
                        while (urs.next()) {
                            final String message = urs.getString("updatePost");
                            postUpdate(message);
                        }
                        IdlePokemon.getBot().getDatabase().update("DELETE FROM updatePosts;");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }, 2 * 60 * 1000, 5 * 60 * 1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setUpdateChannel(Guild guild, TextChannel channel) {
        IdlePokemon.getBot().getDatabase().update("DELETE FROM offServerUpdates WHERE guildId=?;", guild.getId());
        IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO offServerUpdates VALUES (?, ?);", guild.getId(), channel.getId());
        updateChannels.remove(guild.getIdLong());
        updateChannels.put(guild.getIdLong(), channel.getIdLong());
    }

    private static void postUpdate(String message) {
        int sentUpdateSuccessfully = 0;
        int sentUpdateFailure = 0;
        try {
            if (message.contains(":") && message.replaceFirst(":", "").contains(":")) {
                message = message.replaceAll("\\\\:", "\\~;");
                String[] sbtrs = StringUtils.substringsBetween(message, ":", ":");
                List<String> replaced = new ArrayList<>();
                for (String sbtr : sbtrs) {
                    if (replaced.contains(sbtr)) continue;
                    replaced.add(sbtr);
                    message = message.replaceAll(":" + sbtr + ":",
                            EmoteUtil.getEmoteMentionOrDefaultAsString(sbtr, ":" + sbtr + ":"));
                }
                message = message.replaceAll("~;", ":");
            }
        } catch (Exception ex) {
            System.out.println("Error at post update: " + ex.getMessage());
        }
        boolean embed = false;
        if (message.startsWith("EMBED|")) {
            message = message.substring("EMBED|".length());
            embed = true;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Constants.MAIN_COLOR);
        eb.setTitle("Idle Pokémon Update");
        eb.setDescription(message);
        MessageEmbed tm1 = eb.build();
        Message tm2 = new MessageBuilder().setContent("__**Idle Pokémon update**__:\n\n" + message).build();
        for (final long guildId : updateChannels.keySet()) {
            final Guild guild = IdlePokemon.getBot().getShardManager().getGuildById(guildId);
            if (guild == null) continue;
            final long channelId = updateChannels.get(guildId);
            final TextChannel channel = guild.getTextChannelById(channelId);
            if (channel == null) {
                IdlePokemon.getBot().getDatabase()
                        .update("DELETE FROM offServerUpdates WHERE guildId=? AND channelId=?;", guildId, channelId);
                continue;
            }
            Member self = guild.getSelfMember();
            if (self.hasPermission(channel, Permission.MESSAGE_WRITE)) {
                try {
                    if (embed) {
                        if (self.hasPermission(channel, Permission.MESSAGE_EMBED_LINKS)) {
                            channel.sendMessage(tm1).complete();
                        } else {
                            Message tm3 = new MessageBuilder().setContent("__**Idle Pokémon update**__:\n\n" + message +
                                                                          "\n*Please give me the MESSAGE_EMBED_LINKS " +
                                                                          "permission*").build();
                            channel.sendMessage(tm3).complete();
                        }
                    } else {
                        channel.sendMessage(tm2).complete();
                    }
                    sentUpdateSuccessfully++;
                    if (guild.getId().equalsIgnoreCase(Constants.main_guild)) {
                        channel.sendMessage(guild.getRoleById("606261259813257283").getAsMention())
                                .queue(msg -> msg.delete().queueAfter(1, TimeUnit.SECONDS));
                    }
                } catch (Exception ex) {
                    sentUpdateFailure++;
                }
            } else sentUpdateFailure++;
        }
        System.out.println(
                "Sent update post to " + sentUpdateSuccessfully + " servers, failed at " + sentUpdateFailure +
                " (out of " + updateChannels.size() + ")");
    }
}