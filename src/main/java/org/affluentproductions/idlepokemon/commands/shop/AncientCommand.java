package org.affluentproductions.idlepokemon.commands.shop;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.ancient.Ancient;
import org.affluentproductions.idlepokemon.ancient.Ancients;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.math.BigInteger;
import java.util.TreeMap;

public class AncientCommand extends BotCommand {

    public AncientCommand() {
        this.name = "ancient";
        this.cooldown = 1.5;
        this.aliases = new String[]{"ancients", "a"};

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 1) {
            String display = "";
            TreeMap<Integer, Ancient> ancients = new TreeMap<>(Ancients.getAncients());
            for (Ancient ancient : ancients.values()) {
                if (Ancients.hasAncient(uid, ancient)) display +=
                        "`[" + ancient.getID() + "]` - " + ancient.getDisplay() + " | Lv. " +
                        Ancients.getAncientLv(uid, ancient) + " | " + ancient.getDescription() + "\n";
            }
            e.reply(MessageUtil.info("Ancients",
                    "Use `" + Constants.PREFIX + "ancient <Ancient ID> [amount]` to upgrade your ancients " +
                    "(confirmation needed).\n\n" + display + "\nTo buy an ancient, use `" + Constants.PREFIX +
                    "ancient shop`"));
            return;
        }
        Player p = new Player(uid);
        EcoUser eco = p.getEcoUser();
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("shop")) {
            if (args.length < 2) {
                String display = "";
                TreeMap<Integer, Ancient> ancients = new TreeMap<>(Ancients.getAncients());
                for (Ancient ancient : ancients.values()) {
                    if (!Ancients.hasAncient(uid, ancient)) display +=
                            "`[" + ancient.getID() + "]` - " + ancient.getDisplay() + " | " + ancient.getDescription() +
                            "\n";
                }
                int owned = Ancients.getOwnedAncients(uid);
                BigInteger summonCost = Ancients.getSummonCost(owned);
                e.reply(MessageUtil.info("Ancients",
                        "Use `" + Constants.PREFIX + "ancient shop <Ancient ID>` to buy ancients (NO confirmation " +
                        "needed!).\nThe next ancient will cost you " + EmoteUtil.getSoul() + " " +
                        FormatUtil.formatCommas(summonCost) + ".\n\n" + display));
                return;
            }
            int aid;
            try {
                aid = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.err("Error", "The argument `<Ancient ID>` must be a number!"));
                return;
            }
            Ancient ancient = Ancients.getAncient(aid);
            if (ancient == null) {
                e.reply(MessageUtil.err("Error", "This is not a valid ancient ID!"));
                return;
            }
            if (Ancients.hasAncient(uid, ancient)) {
                e.reply(MessageUtil.err("Error", "You already summoned this ancient!"));
                return;
            }
            BigInteger cost = Ancients.getSummonCost(Ancients.getOwnedAncients(uid));
            if (eco.getSouls().compareTo(cost) < 0) {
                e.reply(MessageUtil.err("Error",
                        "You don't have enough " + EmoteUtil.getSoul() + " pokémon souls!\nYou need " +
                        EmoteUtil.getSoul() + " " + FormatUtil.formatCommas(cost) + " to summon this ancient."));
                return;
            }
            eco.removeSouls(cost);
            Ancients.setAncient(uid, ancient, 1);
            e.reply(MessageUtil.success("Ancient", "Successfully summoned " + ancient.getDisplay() + "!"));
            return;
        }
        int aid;
        try {
            aid = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
            e.reply(MessageUtil.err("Error", "The argument `<Ancient ID>` must be a number!"));
            return;
        }
        int amount = 1;
        boolean hasAmountArg = false;
        boolean confirm = false;
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                if (i == (args.length - 1)) {
                    if (args[i].equalsIgnoreCase("confirm")) {
                        confirm = true;
                        continue;
                    }
                }
                try {
                    amount = Integer.parseInt(args[i]);
                    hasAmountArg = true;
                    if (amount < 1) {
                        e.reply(MessageUtil.err("Error", "The argument `[amount]` must be bigger than `0`!"));
                        return;
                    }
                    if (amount > 1000) {
                        e.reply(MessageUtil.err("Error", "The argument `[amount]` must be smaller than `1001`"));
                        return;
                    }
                } catch (NumberFormatException ex) {
                    e.reply(MessageUtil.err("Error", "The argument `[amount]` must be a number!"));
                    return;
                }
            }
        }
        if (hasAmountArg) {
            e.reply(MessageUtil.err("Error",
                    "The argument `[amount]` has been temporarily removed.\nPlease upgrade your ancients one by one " +
                    "until it's re-added - thanks!"));
            return;
        }
        Ancient ancient = Ancients.getAncient(aid);
        if (ancient == null) {
            e.reply(MessageUtil.err("Error", "This is not a valid ancient ID!"));
            return;
        }
        if (!Ancients.hasAncient(uid, ancient)) {
            e.reply(MessageUtil.err("Error",
                    "You didn't summon this ancient yet!\nUse `" + Constants.PREFIX + "ancient shop " + aid +
                    "` to summon it."));
            return;
        }
        int nowLevel = Ancients.getAncientLv(uid, ancient);
        int toLevel = nowLevel + amount;
        if (ancient.getMaxLevel() != -1 && toLevel > ancient.getMaxLevel()) {
            e.reply(MessageUtil.err("Error",
                    "You can't upgrade this ancient that many times!\nThe max. level of this ancient is Lv. " +
                    ancient.getMaxLevel() + "!\nYou can upgrade it up to `" + (ancient.getMaxLevel() - nowLevel) +
                    "` time(s)."));
            return;
        }
        BigInteger cost = ancient.getCost(toLevel);
        if (!confirm) {
            String amountArg = hasAmountArg ? " " + amount : "";
            e.reply(MessageUtil.info("Confirmation",
                    "Are you sure you want to upgrade this ancient to Lv. " + toLevel + " for " + EmoteUtil.getSoul() +
                    " " + FormatUtil.formatCommas(cost) + "?\nUse `" + Constants.PREFIX + "ancient " + aid + amountArg +
                    " confirm`"));
            return;
        }
        if (eco.getSouls().compareTo(cost) < 0) {
            e.reply(MessageUtil.err("Error",
                    "You don't have enough " + EmoteUtil.getSoul() + " pokémon souls!\nYou need " +
                    EmoteUtil.getSoul() + " " + FormatUtil.formatCommas(cost) + " to upgrade this ancient."));
            return;
        }
        eco.removeSouls(cost);
        Ancients.setAncient(uid, ancient, toLevel);
        e.reply(MessageUtil.success("Ancient",
                "Successfully upgraded the " + ancient.getDisplay() + " Ancient to Lv. " + toLevel + "!"));
    }
}