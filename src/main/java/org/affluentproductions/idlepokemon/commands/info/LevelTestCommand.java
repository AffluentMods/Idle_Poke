package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.Formula;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class LevelTestCommand extends BotCommand {

    public LevelTestCommand() {
        this.name = "leveltest";
        this.aliases = new String[]{"lt"};
        this.cooldown = 1.0;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = new Player(uid);
        if (args.length == 0) {
            StringBuilder upgrades = new StringBuilder();
            int lastID = 0;
            HashMap<Integer, Pokemon> pokemons = p.getPokemons();
            for (int ID : pokemons.keySet()) {
                Pokemon pokemon = pokemons.get(ID);
                if (ID > lastID) lastID = ID;
                String costDisplay = pokemon.getLevel() >= 8000 ? "Max." : EmoteUtil.getCoin() + " `" + FormatUtil
                        .formatAbbreviated(Formula.getLevelUpCost(pokemon.getBaseCost(), pokemon.getLevel(), 1));
                upgrades.append("[`").append(ID).append("`] Lv. ").append(pokemon.getLevel()).append(" ")
                        .append(pokemon.getDisplayName()).append(": ").append(costDisplay).append("`\n");
            }
            Pokemon newPokemon = Pokemon.getPokemon(lastID + 1);
            if (newPokemon != null) {
                upgrades.append("[`").append(newPokemon.getID()).append("`] Lv. 0 ").append(newPokemon.getName())
                        .append(" (unowned): ").append(EmoteUtil.getCoin()).append(" `")
                        .append(FormatUtil.formatAbbreviated(newPokemon.getBaseCost())).append("`");
            }
            upgrades.append("\n\nUse `" + Constants.PREFIX + "level <ID> [amount]` to level up a pokémon!");
            String a = upgrades.toString();
            if (a.length() > 2000) {
                String first = a.substring(0, 2000).substring(0, a.lastIndexOf("`"));
                String second = a.substring(first.length());
                e.reply(MessageUtil.info("Pokémons", first));
                e.reply(MessageUtil.info("Pokémons", second));
                return;
            }
            e.reply(MessageUtil.info("Pokémons", upgrades.toString()));
            return;
        }
        int levelAmount = 1;
        int pid;
        try {
            pid = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err("Invalid argument", "The argument `<ID>` must be a number!"));
            return;
        }
        try {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("all")) levelAmount = -3;
                else {
                    levelAmount = Integer.parseInt(args[1]);
                    if (levelAmount <= 0 || levelAmount > 500) {
                        e.reply(MessageUtil
                                .err("Invalid argument", "The argument `[amount]` must be between `1` and `500`!"));
                        return;
                    }
                }
            }
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err("Invalid argument", "The argument `[amount]` must be a number!"));
            return;
        }
        TreeMap<Integer, Pokemon> pokemons = new TreeMap<>(p.getPokemons());
        List<Integer> pids = new ArrayList<>(pokemons.keySet());
        int nextPID = pids.size() > 0 ? pids.get(pids.size() - 1) + 1 : 1;
        boolean pidTooHigh = pid > nextPID;
        Pokemon pokemon = Pokemon.getPokemon(pid);
        if (pidTooHigh || pokemon == null) {
            e.reply(MessageUtil
                    .err("Error", "You didn't unlock the previous pokémon with the ID `" + nextPID + "` yet!"));
            return;
        }
        int oldLevel = pokemons.containsKey(pid) ? pokemons.get(pid).getLevel() : 0;
        if (oldLevel >= 2147000000L) {
            e.reply(MessageUtil.err("Error", "You already maxed this pokémon's level!"));
            return;
        }
        if (oldLevel + levelAmount > 2147000000L) {
            e.reply(MessageUtil.err("Error",
                    "You can't level the pokémon up to over Lv. 2,147,000,000!\nYou can upgrade it up to `" +
                    (2147000000 - oldLevel) + "` time(s)!"));
            return;
        }
        EcoUser ecoUser = p.getEcoUser();
        BigInteger coins = ecoUser.getCoins();
        if (levelAmount == -3) {
            for (int i = 1; i <= 100; i++) {
                BigInteger c = Formula.getLevelUpCost(pokemon.getBaseCost(), oldLevel, i);
                levelAmount = i;
                if (coins.compareTo(c) < 0) {
                    levelAmount--;
                    break;
                }
            }
        }
        BigInteger cost;
        if (levelAmount <= 0) cost = Formula.getLevelUpCost(pokemon.getBaseCost(), oldLevel, 1);
        else cost = Formula.getLevelUpCost(pokemon.getBaseCost(), oldLevel, levelAmount);
        e.reply(MessageUtil.info("Cost",
                "Leveling " + pokemon.getDisplayName() + " to Lv. " + (oldLevel + levelAmount) + " would cost `" +
                FormatUtil.formatAbbreviated(cost) + "` " + EmoteUtil.getCoin()));
    }
}