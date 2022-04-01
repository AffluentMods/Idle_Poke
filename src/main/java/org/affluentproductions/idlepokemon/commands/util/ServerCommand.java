package org.affluentproductions.idlepokemon.commands.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.util.ServerUtil;

import java.util.List;

public class ServerCommand extends BotCommand {

    public ServerCommand() {
        this.name = "server";

        this.cooldown = 1;
    }

    @Override
    public void execute(CommandEvent e) {
        Member m = e.getMember();
        if (m.hasPermission(Permission.MANAGE_SERVER)) {
            String[] args = e.getArgs();
            if (args.length == 0) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + Constants.PREFIX + "server <setupdatechannel> <channel>`"));
                return;
            }
            int al = args.length;
            String arg = args[0].toLowerCase();
            if (arg.equals("setupdatechannel")) {
                TextChannel c = null;
                if (al > 1) {
                    List<TextChannel> mentionedChannels = e.getMessage().getMentionedChannels();
                    if (mentionedChannels.size() > 0) {
                        c = mentionedChannels.get(0);
                    } else {
                        StringBuilder channelName = new StringBuilder();
                        for (int i = 1; i < args.length; i++) channelName.append(args[i]).append(" ");
                        if (channelName.toString().endsWith(" "))
                            channelName = new StringBuilder(channelName.substring(0, channelName.length() - 1));
                        List<TextChannel> foundChannels =
                                m.getGuild().getTextChannelsByName(channelName.toString(), false);
                        if (foundChannels.size() > 0) {
                            c = foundChannels.get(0);
                        } else {
                            e.reply(MessageUtil.err("Error", "Could not find any channel by that name!"));
                            return;
                        }
                    }
                }
                if (c == null) {
                    e.reply(MessageUtil.err("Error", "Please use `" + Constants.PREFIX +
                                                     "server setupdatechannel <#channel>` to set a channel for " +
                                                     "updates.\nThis channel will be used to automatically send " +
                                                     "change logs from the Idle Pokémon bot."));
                    return;
                }
                ServerUtil.setUpdateChannel(m.getGuild(), c);
                e.reply(MessageUtil.success("Update channel set", "Set the update-channel to " + c.getAsMention() +
                                                                  "\nMake sure the bot has permissions to send " +
                                                                  "messages."));
                return;
            }
            return;
        }
        e.reply(MessageUtil
                .err("Error", "You need the permission `MANAGE_SERVER` to use this command in this server!"));
    }
}