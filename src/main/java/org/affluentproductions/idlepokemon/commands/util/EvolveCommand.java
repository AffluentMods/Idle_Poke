package org.affluentproductions.idlepokemon.commands.util;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

public class EvolveCommand extends BotCommand {

    public EvolveCommand() {
        this.name = "evolve";
        this.cooldown = 1.7;

    }

    private static final int pid = 20;

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = new Player(uid);
        if (!p.hasUpgrade(pid, 4)) {
            e.reply(MessageUtil.err("Error",
                    "You need the Evolution Upgrade from the pokémon " + Pokemon.getPokemon(pid).getDisplayName() +
                    " to evolve!"));
            return;
        }
        long totalLevels = p.getTotalLevels();
        long obtainableSouls = p.getObtainableSouls();
        if (args.length < 1) {
            e.reply(MessageUtil.info("Evolve",
                    "Your Pokémon, Coins and Stage will be reset. You will receive " + EmoteUtil.getSoul() + " `" +
                    obtainableSouls + "` Souls!\nTotal Pokémon Levels: `" + FormatUtil.formatCommas(totalLevels) +
                    "` (<- this divided by 2,000 is the amount of souls you receive)\nUse `" + Constants.PREFIX +
                    "evolve confirm` to evolve!"));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("confirm")) {
            p.evolve(true);
            e.reply(MessageUtil.success("Evolve",
                    "Successfully evolved!\nCongratulations! You received " + EmoteUtil.getSoul() + " `" +
                    obtainableSouls + "` Souls!"));
            p.updateDPS();
            p.updateCD();
            return;
        }
        e.reply(MessageUtil.err("Error", "Invalid argument"));
    }
}