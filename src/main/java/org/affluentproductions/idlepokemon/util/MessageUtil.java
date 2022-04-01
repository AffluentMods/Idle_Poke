package org.affluentproductions.idlepokemon.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageUtil {

    public static Message newMsg(Color color, String message, String embedTitle, String embedDescription) {
        Message msg;
        MessageBuilder builder = new MessageBuilder(message);
        builder.setEmbed(
                new EmbedBuilder().setTitle(embedTitle).setDescription(embedDescription).setColor(color).build());
        msg = builder.build();
        return msg;
    }

    public static MessageEmbed info(String title, String message) {
        return info(title, message, false);
    }

    public static MessageEmbed info(String title, String message, boolean timestamp) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(message);
        eb.setColor(new Color(0, 119, 255));
        if (timestamp) eb.setTimestamp(Instant.now());
        return eb.build();
    }

    public static MessageEmbed success(String title, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(message);
        eb.setColor(new Color(2, 199, 13));
        return eb.build();
    }

    public static MessageEmbed err(String title, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(message);
        eb.setColor(new Color(255, 52, 40));
        return eb.build();
    }

    public static boolean sendMessage(User u, String msg1, boolean presuffix) {
        if (presuffix) msg1 = "You received a message from an **Admin** of **Idle Pokémon**:\n\n`" + msg1 +
                              "`\n\nTo respond, please visit the official discord server of Idle Pokémon.\nClick the " +
                              "link" + " at `" + Constants.PREFIX + "invite` to join.";
        final String msg = msg1;
        final AtomicBoolean sent = new AtomicBoolean(false);
        u.openPrivateChannel().queue(s -> s.sendMessage(msg).queue(ss -> sent.set(true), f -> sent.set(false)));
        return true;
    }
}