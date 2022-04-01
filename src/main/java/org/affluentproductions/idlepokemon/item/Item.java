package org.affluentproductions.idlepokemon.item;

import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.FormattedString;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class Item {

    private static final HashMap<String, Item> items = new HashMap<>();

    public static HashMap<String, Item> getItems() {
        return items;
    }

    private final String displayName;
    private final ItemAction atPurchase;
    private final int rubyPrice;
    private final String description;
    private final boolean evolutionState;
    private final boolean megaEvolutionState;
    private final int stockPerUser;
    private final Bonus bonus;

    public Item(String displayName, int rubyPrice, String description, boolean evolutionState,
                boolean megaEvolutionState, int stockPerUser, Bonus bonus, ItemAction atPurchase) {
        this.displayName = displayName;
        this.atPurchase = atPurchase;
        this.rubyPrice = rubyPrice;
        this.description = description;
        this.evolutionState = evolutionState;
        this.megaEvolutionState = megaEvolutionState;
        this.stockPerUser = stockPerUser;
        this.bonus = bonus;
    }

    public boolean isNeedsConfirmation() {
        return false;
    }

    public FormattedString getConfirmationMessage() {
        return new FormattedString(
                "Are you sure you want to use {item}?\nUse `" + Constants.PREFIX + "shop {item} confirm` to confirm.",
                Collections.singletonList("item")) {
            @Override
            public String format(Player player, String placeholder, String string) {
                if (placeholder.equalsIgnoreCase("item")) return string.replace(placeholder, displayName);
                return string;
            }
        };
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemAction getAtPurchase() {
        return atPurchase;
    }

    public int getRubyPrice(Player p) {
        return rubyPrice;
    }

    public String getDescription() {
        return description;
    }

    public boolean getEvolutionState() {
        return evolutionState;
    }

    public boolean getMegaEvolutionState() {
        return megaEvolutionState;
    }

    public int getStockPerUser() {
        return stockPerUser;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public static Item getItem(String displayName) {
        return items.get(displayName.toLowerCase());
    }

    public static void load() {
        List<Item> items1 = Arrays.asList(new DoubleDamage(), new TimeLapse1(), new TimeLapse2(), new TimeLapse3(),
                new TimeLapse4(), new AutoClicker(), new QuickEvolution(), new ThreeShinyPokemon());
        for (Item item : items1) {
            items.put(item.getDisplayName().toLowerCase(), item);
        }
    }
}