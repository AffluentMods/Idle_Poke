package org.affluentproductions.idlepokemon.item;

import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

public class AutoClicker extends Item {

    public AutoClicker() {
        super("Auto Clicker", 100, "Automatically clicks 1 time every second.", true, true, 1000000,
                new Bonus(0, BonusType.NULL), new ItemAction() {
                    @Override
                    public void run(Player player, TextChannel tc) {
                    }
                });
    }

    @Override
    public int getRubyPrice(Player p) {
        int rubyPrice = 100;
        int autoClickersAmount = p.getProducts().getOrDefault("auto clicker", 0);
        return rubyPrice + (50 * autoClickersAmount);
    }
}