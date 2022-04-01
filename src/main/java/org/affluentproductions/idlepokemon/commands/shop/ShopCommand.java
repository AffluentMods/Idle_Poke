package org.affluentproductions.idlepokemon.commands.shop;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.item.Item;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.math.BigInteger;
import java.util.TreeMap;

public class ShopCommand extends BotCommand {

    public ShopCommand() {
        this.name = "shop";
        this.cooldown = 0.7;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = new Player(uid);
        EcoUser ecoUser = p.getEcoUser();
        BigInteger rubies = ecoUser.getRubies();
        if (args.length < 1) {
            StringBuilder reply = new StringBuilder(
                    "• Use `" + Constants.PREFIX + "shop <item>` to purchase an item here.\n\n**Name | Effect | " +
                    "Cost**\n");
            TreeMap<String, Item> items = new TreeMap<>(Item.getItems());
            for (String itemName : items.keySet()) {
                Item item = items.get(itemName);
                reply.append("• ").append(item.getDisplayName()).append(" | ").append(item.getDescription())
                        .append(" | ").append(EmoteUtil.getRuby()).append(" `")
                        .append(FormatUtil.formatCommas(item.getRubyPrice(p))).append("`\n");
            }
            reply.append("\nYou currently have: ").append(EmoteUtil.getRuby()).append(" ")
                    .append(FormatUtil.formatCommas(rubies));
            e.reply(MessageUtil.info("Ruby Shop", reply.toString()));
            return;
        }
        boolean isConfirm = false;
        StringBuilder itemName = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == (args.length - 1)) {
                if (arg.equalsIgnoreCase("confirm")) {
                    isConfirm = true;
                    continue;
                }
            }
            itemName.append(arg).append(" ");
        }
        if (itemName.toString().endsWith(" "))
            itemName = new StringBuilder(itemName.substring(0, itemName.length() - 1));
        Item item = Item.getItem(itemName.toString().toLowerCase());
        if (item == null) {
            e.reply(MessageUtil.err("Error", "This item does not exist."));
            return;
        }
        int own = p.getProducts().getOrDefault(item.getDisplayName().toLowerCase(), 0);
        if (own >= item.getStockPerUser()) {
            e.reply(MessageUtil.err("Error", "You reached the limit of the `" + item.getDisplayName() +
                                             "` product.\nYou can't buy another one!"));
            return;
        }
        BigInteger rubyPrice = new BigInteger(item.getRubyPrice(p) + "");
        if (rubies.compareTo(rubyPrice) < 0) {
            e.reply(MessageUtil.err("Error",
                    "You need " + EmoteUtil.getRuby() + " `" + FormatUtil.formatCommas(rubyPrice) +
                    "` to buy this item!\nUse `" + Constants.PREFIX +
                    "invite` to join the support server to enter giveaways for " + EmoteUtil.getRuby() +
                    " or buy some with money.\nYou can also vote using `" + Constants.PREFIX + "vote` to get some " +
                    EmoteUtil.getRuby() + "."));
            return;
        }
        if (item.isNeedsConfirmation()) {
            if (!isConfirm) {
                e.reply(MessageUtil.info("Confirm", item.getConfirmationMessage().getString(p)));
                return;
            }
        }
        ecoUser.removeRubies(rubyPrice);
        p.setProduct(item.getDisplayName().toLowerCase(), own + 1);
        item.getAtPurchase().run(p, e.getMessage().getTextChannel());
        p.updateDPS();
        p.updateCD();
        e.reply(MessageUtil.success("Success", "Successfully bought " + item.getDisplayName() + "!"));
    }
}