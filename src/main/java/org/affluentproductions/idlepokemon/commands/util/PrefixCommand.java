package org.affluentproductions.idlepokemon.commands.util;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.MessageUtil;

public class PrefixCommand extends BotCommand {

    public PrefixCommand() {
        this.name = "prefix";
        this.cooldown = 0.5;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = new Player(uid);
        if (args.length < 1) {
            e.reply(MessageUtil.info("Prefix",
                    "Your current prefix is `" + p.getPrefix() + "`\nUse `" + Constants.PREFIX +
                    "prefix <new prefix>` to change it (up to 4 characters)."));
            return;
        }
        String arg = args[0];
        if (arg.length() > 4) {
            e.reply(MessageUtil.
                    err("Error", "The argument `<prefix>` can not be longer than `4` characters!"));
            return;
        }
        try {
            p.setPrefix(arg);
        } catch (Exception ex) {
            String defaultPrefix = IdlePokemon.test ? Constants.TEST_PREFIX : Constants.PREFIX;
            e.reply(MessageUtil.err("Error",
                    "Could not set prefix! Make sure it doesn't contain characters which could be not readable!\nYour" +
                    " prefix got reset to `" +
                    defaultPrefix + "`"));
            return;
        }
        e.reply(MessageUtil
                .success("Prefix changed", "Successfully changed the Idle Pokémon prefix to `" + arg + "`!"));
    }
}