package org.affluentproductions.idlepokemon.commands;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.MessageUtil;

public class PresetCommand extends BotCommand {

    public PresetCommand() {
        this.name = "preset";
        this.cooldown = 0.5;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length == 0) {
            e.reply(MessageUtil.info("Usage", "Please use `" + Constants.PREFIX + this.name + " <../../..>`."));
            return;
        }
    }
}