package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.util.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class InfoCommand extends BotCommand {

    public InfoCommand() {
        this.name = "info";
        this.cooldown = 1.5;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length == 0) {
            e.reply(MessageUtil.info("Usage", "Please use `" + Constants.PREFIX + this.name + " <pokémon / ID>`"));
            return;
        }
        Player p = new Player(uid);
        Pokemon pokemon;
        boolean dev = false;
        StringBuilder arg = new StringBuilder();
        for (String s : args) {
            if (s.equalsIgnoreCase("--dev")) {
                dev = true;
                continue;
            }
            arg.append(s).append(" ");
        }
        if (arg.toString().endsWith(" ")) arg = new StringBuilder(arg.substring(0, arg.length() - 1));
        try {
            int ID = Integer.parseInt(arg.toString());
            HashMap<Integer, Pokemon> rids = p.getPokemons();
            int nextID = new ArrayList<>(rids.keySet()).get(rids.size() - 1) + 1;
            if (ID == nextID) pokemon = Pokemon.getPokemon(ID, 1);
            else pokemon = rids.get(ID);
        } catch (NumberFormatException ex) {
            pokemon = p.getPokemonByName(arg.toString());
        }
        if (pokemon == null) {
            if (dev) pokemon = Pokemon.getPokemon(Integer.parseInt(arg.toString()), 1);
            else {
                e.reply(MessageUtil.err("Error", "You don't own a pokémon with this name or ID: `" + arg + "`"));
                return;
            }
        }
        final int ID = pokemon.getID();
        final String name = pokemon.getName();
        final int level = pokemon.getLevel();
        final BigDecimal cd = pokemon.getCd();
        final BigDecimal dps = pokemon.getDps();
        final BigInteger basecost = pokemon.getBaseCost();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Constants.MAIN_COLOR);
        eb.setTitle(name + " Info");
        eb.addField("ID", "Pokémon ID: `" + ID + "`", true);
        eb.addField("Level", "Lv. " + level, true);
        String preDamageString = "";
        if (cd.compareTo(BigDecimal.ZERO) > 0) {
            preDamageString += "Base Click Damage: `" + FormatUtil.formatAbbreviated(cd) + "`\n";
            preDamageString +=
                    "Click Damage: `" + FormatUtil.formatAbbreviated(p.getClickDamageOfPokemon(pokemon)) + "`\n";
        }
        eb.addField("DPS", preDamageString + "Base DPS: `" + FormatUtil.formatAbbreviated("" + dps) + "`\nDamage: `" +
                           FormatUtil.formatAbbreviated(p.getDPSofPokemon(pokemon)) + "` per second", true);
        eb.addField("Cost", "Base Cost: `" + FormatUtil.formatAbbreviated(basecost) + "` " + EmoteUtil.getCoin() +
                            "\nLevel Cost: `" +
                            FormatUtil.formatAbbreviated(Formula.getLevelUpCost(basecost, level, 1)) + "` " +
                            EmoteUtil.getCoin(), true);
        StringBuilder upgradesString = new StringBuilder("• Use `" + Constants.PREFIX + "upgrade " + ID +
                                                         " <upgrade ID>` to buy an upgrade. The Upgrade `ID` is on " +
                                                         "the left.\n");
        for (int upgradeID : pokemon.getUpgrades().keySet()) {
            final Upgrade upgrade = pokemon.getUpgrades().get(upgradeID);
            String upgradeDisplay = upgrade.getUpgradeDisplay();
            final String checkX =
                    p.hasUpgrade(ID, upgradeID) ? EmoteUtil.getEmoteMention("check") : EmoteUtil.getEmoteMention("X_");
            upgradesString.append("[`").append(upgrade.getUpgradeID()).append("`] **Lv. ").append(upgrade.getMinLevel())
                    .append("** | ").append(upgradeDisplay).append(" | ").append(EmoteUtil.getCoin()).append(" `")
                    .append(FormatUtil.formatAbbreviated(upgrade.getCost())).append("` ").append(checkX).append("\n");
        }
        eb.addField("Upgrades", upgradesString.toString(), false);
        if (ShinyUtil.getShinies(uid, pokemon) > 0)
            eb.setImage("https://www.affluentproductions.org/pokeimages/" + ID + "_shiny.png");
        else eb.setImage("https://www.affluentproductions.org/pokeimages/" + ID + ".png");
        eb.setAuthor(u.getAsTag());
        e.reply(eb.build());
    }
}