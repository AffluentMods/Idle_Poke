package org.affluentproductions.idlepokemon.commands.info;

import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.MessageUtil;

public class StartCommand extends BotCommand {

    public StartCommand() {
        this.name = "start";
        this.cooldown = 0.5;

    }

    @Override
    public void execute(CommandEvent e) {
        e.reply(MessageUtil.info("Start",
                "Thanks for playing Idle Pokémon!\nTo start, you need to click: just use `pclick`.\nCheck your " +
                "pokémons, coins and other information with `pinv`.\nNeed more help? Use `phelp` or `pguides` to view" +
                " complete guides."));
    }
}