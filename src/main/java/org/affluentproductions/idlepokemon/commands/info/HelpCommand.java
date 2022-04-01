package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;

public class HelpCommand extends BotCommand {

    public HelpCommand() {
        this.name = "help";
        this.cooldown = 1;

    }

    @Override
    public void execute(CommandEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Constants.MAIN_COLOR);
        eb.setTitle("Help");
        eb.setDescription(
                "• Use `" + Constants.PREFIX + "start` to start playing.\n\n• You can use `" + Constants.PREFIX +
                "guides` for a list of guides.");
        eb.addField("Website", "https://www.affluentproductions.org/", true);
        //eb.addField("Commands & Information", "https://www.affluentproductions.org/?s=idlepokemon_doc", false);
        eb.addField("Support Server", "https://invite.affluentproductions.org/apserver", false);
        eb.addField("Known Bug List", "https://www.affluentproductions.org/?s=bugs", true);
        e.getMessage().getChannel().sendMessage(eb.build()).queue();
    }
}