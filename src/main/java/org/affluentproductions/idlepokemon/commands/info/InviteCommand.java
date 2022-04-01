package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;

public class InviteCommand extends BotCommand {

    public InviteCommand() {
        this.name = "invite";
        this.cooldown = 1;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Idle Pokémon");
        eb.addField("Support Server", "https://invite.affluentproductions.org/apserver", false);
        eb.addField("Invite the bot to your server", "https://invite.affluentproductions.org/idlepokemon", false);
        eb.setAuthor(u.getAsTag());
        e.reply(eb.build());
    }
}