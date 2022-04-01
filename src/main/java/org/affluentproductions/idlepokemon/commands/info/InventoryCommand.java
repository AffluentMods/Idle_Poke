package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.commands.util.PrizeCommand;
import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class InventoryCommand extends BotCommand {

    public InventoryCommand() {
        this.name = "inventory";
        this.cooldown = 1;

        this.aliases = new String[]{"inv"};
    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        User mentioned = MentionUtil.getUser(e.getMessage());
        Player p;
        if (mentioned != null) p = new Player(mentioned.getId(), false);
        else {
            p = new Player(uid);
            p.updateActive();
        }
        int page = 1;
        String[] args = e.getArgs();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == (args.length - 1)) {
                try {
                    page = Integer.parseInt(arg);
                    if (page <= 0) {
                        e.reply(MessageUtil.err("Error", "The argument `[page]` must be bigger than `0`!"));
                        return;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Constants.MAIN_COLOR);
        eb.setTitle(p.getAsTag());
        StringBuilder badgesString = new StringBuilder();
        for (String badge : p.getBadges()) {
            badgesString.append(EmoteUtil.getEmoteMention(badge + "_badge")).append(" ");
        }
        eb.appendDescription("**Badges:** " + badgesString + "\n");
        String cdString = FormatUtil.formatAbbreviated(p.getClickDamage().toBigInteger().toString(), 5);
        eb.appendDescription("**Click Damage:** `" + cdString + "`\n");
        String dpsString = FormatUtil.formatAbbreviated("" + p.getDPS().toBigInteger().toString(), 5);
        eb.appendDescription("**DPS:** `" + dpsString + "`/s\n");
        EcoUser ecoUser = p.getEcoUser();
        String coins = FormatUtil.formatAbbreviated("" + ecoUser.getCoins(), 5);
        String rubies = FormatUtil.formatCommas(ecoUser.getRubies());
        String souls = FormatUtil.formatCommas(ecoUser.getSouls());
        eb.appendDescription(
                EmoteUtil.getCoin() + " " + coins + Constants.TAB + Constants.TAB + " " + EmoteUtil.getRuby() + " " +
                rubies + Constants.TAB + Constants.TAB + " " + EmoteUtil.getSoul() + " " + souls);
        eb.appendDescription("\n");
        TreeMap<Integer, Pokemon> unsortedPokemons = new TreeMap<>(p.getPokemons());
        TreeMap<Integer, Pokemon> pokemons = new TreeMap<>();
        for (int pid : unsortedPokemons.keySet()) {
            Pokemon pokemon = unsortedPokemons.get(pid);
            pokemons.put(pokemon.getID(), pokemon);
        }
        int maxPage = getMaxPage(pokemons.size());
        if (page > maxPage) {
            e.reply(MessageUtil
                    .err("Error", "This page does not exist!\nYou can only list page `1` to `" + maxPage + "`"));
            return;
        }
        StringBuilder pokemonsDisplay =
                new StringBuilder("**Pokémons [Page " + page + "/" + maxPage + "]:** `pinv <page>`\n");
        int offset = page * 15 - 14;
        int end = page * 15;
        List<Integer> pids = new ArrayList<>(pokemons.keySet());
        int lastID = pids.size() > 0 ? pids.get(pids.size() - 1) : 0;
        int i = 0;
        for (int ID : pokemons.keySet()) {
            i++;
            if (i < offset) continue;
            if (i > end) break;
            Pokemon pok = pokemons.get(ID);
            int shinies = ShinyUtil.getShinies(uid, pok);
            String shinyDisplay = shinies > 0 ? (shinies == 1 ? " (1 shiny)" : " (" + shinies + " shinies)") : "";
            pokemonsDisplay.append("[`").append(ID).append("`] ").append(pok.getEmote()).append(" Lv. ")
                    .append(pok.getLevel()).append(" ").append(pok.getName()).append(shinyDisplay).append("\n");
        }
        Pokemon nextPok = Pokemon.getPokemon(lastID + 1);
        if (nextPok != null) {
            pokemonsDisplay.append("[`").append(nextPok.getID()).append("`] ").append(nextPok.getDisplayName())
                    .append(" - Unowned | `").append(Constants.PREFIX).append("level ").append(nextPok.getID())
                    .append("`\n");
        }
        eb.appendDescription(pokemonsDisplay + "\n");
        eb.appendDescription(
                "*Want more information about a pokémon?\nUse `" + Constants.PREFIX + "info <pokémon>` or `" +
                Constants.PREFIX + "info <number>` (the number of the pokémon can be found on the left of its name)*");
        final String iconUrl = "https://www.affluentproductions.org/favicon-32x32.png";
        if (PrizeCommand.enabled) {
            eb.setFooter("by Affluent Productions | A prize is available! Use " + Constants.PREFIX + "prize to redeem.", iconUrl);
        } else eb.setFooter("by Affluent Productions", iconUrl);
        e.reply(eb.build());
        if (!ClickUtil.clickers.contains(uid)) e.reply(MessageUtil.info("Information",
                "You don't deal DPS automatically yet!\nIf you are new, you should be automatically added to the " +
                "DPS-Clicker list within the next 1-5 minutes and this message will disappear.\nIf you think this is " +
                "an error or this message is appearing too long, report it in our support server: `pinvite`"));
    }

    private int getMaxPage(int size) {
        double s = size / 15.0;
        int maxPage = new Double("" + s).intValue();
        if (size % 15 != 0) maxPage++;
        if (maxPage < 1) maxPage = 1;
        return maxPage;
    }
}