package org.affluentproductions.idlepokemon.botlistener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.util.MessageUtil;

public class BotInviteListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        Guild g = e.getGuild();
        TextChannel botlogChannel = IdlePokemon.getBotLog();
        if (g.getOwner() == null) return;
        botlogChannel.sendMessage(MessageUtil.success("I joined a new guild!",
                "**Guild name:** " + g.getName() + "\n**Members:** " + g.getMembers().size() + "\n**Owner:** " +
                g.getOwner().getUser().getAsTag() + "\nI am in **" + IdlePokemon.getBot().getTotalGuilds() +
                " guilds** now!")).queue();
        IdlePokemon.updateStatus();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        Guild g = e.getGuild();
        TextChannel botlogChannel = IdlePokemon.getBotLog();
        Member owner = g.getOwner();
        if (owner == null) return;
        botlogChannel.sendMessage(MessageUtil.err("I left a guild!",
                "**Guild name:** " + g.getName() + "\n**Members:** " + g.getMembers().size() + "\n**Owner:** " +
                owner.getUser().getAsTag() + "\nI am in **" + IdlePokemon.getBot().getTotalGuilds() + " guilds** now!"))
                .queue();
        IdlePokemon.updateStatus();
        User user = owner.getUser();
        user.openPrivateChannel().queue(success -> success.sendMessage(
                "Hello!\nI would like to ask you, why you kicked our bot (me). No hurt feelings we're just " +
                "curious and would like to know your input so we can better the experience to any future users :)" +
                "\n\nPlease type in your feedback.\n`Your next messages will get redirected to the staff team of " +
                "Idle Pokémon. If you are done with sending feedback, simply type:` `done`").queue(success2 -> {
            FeedbackListener.waitingFeedback.add(user.getId());
        }));
    }
}