package org.affluentproductions.idlepokemon.commands.shop;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.event.pokemon.PokemonUpgradeEvent;
import org.affluentproductions.idlepokemon.manager.EventManager;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpgradeCommand extends BotCommand {

    public UpgradeCommand() {
        this.name = "upgrade";
        this.aliases = new String[]{"u"};
        this.cooldown = 1.75;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length == 0) {
            e.reply(MessageUtil.info("Usage", "Please use `" + Constants.PREFIX +
                                              "upgrade <pokemon ID> <upgrade ID>` to buy an upgrade.\nYou can see all" +
                                              " upgrades using `" + Constants.PREFIX + "info <pokemon ID>`"));
            return;
        }
        String arg = args[0].toLowerCase();
        int pid;
        try {
            pid = Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err("Error", "The argument `<pokemon ID>` must be a number!"));
            return;
        }
        Player p = new Player(uid);
        Pokemon pokemon = p.getPokemon(pid);
        if (pokemon == null) {
            e.reply(MessageUtil.err("Error", "You don't own this pokémon!"));
            return;
        }
        HashMap<Integer, Upgrade> upgrades = pokemon.getUpgrades();
        if (args.length < 2) {
            StringBuilder reply = new StringBuilder(pokemon.getName() + "'s Upgrades:\n\n");
            for (int uID : upgrades.keySet()) {
                Upgrade upgrade = upgrades.get(uID);
                String upgradeDisplay = upgrade.getUpgradeDisplay();
                final String checkX =
                        p.hasUpgrade(pid, upgrade.getUpgradeID()) ? EmoteUtil.getEmoteMention("check") : EmoteUtil
                                .getEmoteMention("X_");
                reply.append("[`").append(upgrade.getUpgradeID()).append("`] **Lv. ").append(upgrade.getMinLevel())
                        .append("** | ").append(upgradeDisplay).append(" | ").append(EmoteUtil.getCoin()).append(" `")
                        .append(FormatUtil.formatAbbreviated(upgrade.getCost())).append("` ").append(checkX)
                        .append("\n");
            }
            reply.append("\nUse `" + Constants.PREFIX + "upgrade ").append(pid)
                    .append(" <upgrade ID>` to buy an upgrade.");
            e.reply(MessageUtil.info("Upgrade", reply.toString()));
            return;
        }
        if (args[1].equalsIgnoreCase("all")) {
            List<Integer> bought = new ArrayList<>();
            boolean tooFewMoney = false;
            boolean tooLowLevel = false;
            EcoUser ecoUser = p.getEcoUser();
            BigInteger coins = ecoUser.getCoins();
            BigInteger totalCost = BigInteger.ZERO;
            for (int uID : upgrades.keySet()) {
                if (p.hasUpgrade(pid, uID)) continue;
                Upgrade upgrade = upgrades.get(uID);
                if (upgrade.getMinLevel() > pokemon.getLevel()) {
                    tooLowLevel = true;
                    break;
                }
                BigInteger cost = upgrade.getCost().toBigInteger();
                if (coins.compareTo(cost) < 0) {
                    tooFewMoney = true;
                    break;
                }
                totalCost = totalCost.add(cost);
                buyUpgrade(p, pokemon, upgrade);
                bought.add(uID);
            }
            if (bought.size() > 0) {
                ecoUser.removeCoins(totalCost);
                StringBuilder bs = new StringBuilder("Successfully bought Upgrade ");
                for (int b : bought) bs.append("`").append(b).append("`, ");
                if (bs.toString().endsWith(", ")) bs = new StringBuilder(bs.substring(0, bs.length() - 2));
                bs.append(" of ").append(pokemon.getDisplayName()).append(" for ").append(EmoteUtil.getCoin())
                        .append(" `").append(FormatUtil.formatAbbreviated(totalCost)).append("`!");
                e.reply(MessageUtil.success("Upgrade", bs.toString()));
                return;
            }
            if (tooLowLevel) {
                e.reply(MessageUtil.err("Error", "Your pokémon has a too low level to buy an upgrade!"));
                return;
            }
            if (tooFewMoney) {
                e.reply(MessageUtil.err("Error", "You need more " + EmoteUtil.getCoin() + " coins to buy an upgrade!"));
                return;
            }
            e.reply(MessageUtil.err("Error", "You already bought all upgrades of this pokémon!"));
            return;
        }
        int uID;
        try {
            uID = Integer.parseInt(args[1]);
            if (!upgrades.containsKey(uID)) {
                e.reply(MessageUtil.err("Error", "This upgrade ID does not exist!"));
                return;
            }
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err("Error", "The argument `<upgrade ID>` must be a number!"));
            return;
        }
        if (p.hasUpgrade(pid, uID)) {
            e.reply(MessageUtil.err("Error", "You already bought this upgrade!"));
            return;
        }
        Upgrade upgrade = upgrades.get(uID);
        if (upgrade.getMinLevel() > pokemon.getLevel()) {
            e.reply(MessageUtil.err("Error",
                    "Your " + pokemon.getDisplayName() + " needs to be Lv. " + upgrade.getMinLevel() +
                    " or higher to unlock this upgrade!"));
            return;
        }
        BigInteger cost = upgrade.getCost().toBigInteger();
        EcoUser ecoUser = p.getEcoUser();
        BigInteger coins = ecoUser.getCoins();
        if (coins.compareTo(cost) < 0) {
            e.reply(MessageUtil.err("Error",
                    "You need `" + FormatUtil.formatAbbreviated(cost) + "` " + EmoteUtil.getCoin() +
                    " to buy this upgrade!"));
            return;
        }
        ecoUser.removeCoins(cost);
        buyUpgrade(p, pokemon, upgrade);
        e.reply(MessageUtil.success("Upgrade",
                "Successfully bought the upgrade `" + upgrade.getUpgradeID() + "` for " + pokemon.getDisplayName() +
                "!"));
    }

    private void buyUpgrade(Player p, Pokemon pokemon, Upgrade upgrade) {
        p.addBoughtUpgrade(pokemon, upgrade);
        if (upgrade.getUpgradeData().getBonus().getBonusType() != BonusType.NULL)
            p.addBonus(pokemon.getName() + " U" + upgrade.getUpgradeID(), upgrade.getUpgradeData().getBonus());
        p.updateDPS();
        p.updateCD();
        EventManager.callEvent(new PokemonUpgradeEvent(p.getUserId(), pokemon, upgrade));
    }
}