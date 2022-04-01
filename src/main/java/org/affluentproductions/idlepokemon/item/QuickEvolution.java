package org.affluentproductions.idlepokemon.item;

import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.FormattedString;
import org.affluentproductions.idlepokemon.util.EmoteUtil;

import java.util.Arrays;

public class QuickEvolution extends Item {

    public QuickEvolution() {
        super("Quick Evolution", 250, "You evolve without needing to restart, everything is kept.", false, false,
                1000000, new Bonus(0, BonusType.NULL), new ItemAction() {
                    @Override
                    public void run(Player player, TextChannel tc) {
                        player.evolve(false);
                        player.updateDPS();
                        player.updateCD();
                    }
                });
    }

    @Override
    public boolean isNeedsConfirmation() {
        return true;
    }

    @Override
    public FormattedString getConfirmationMessage() {
        return new FormattedString("Use `" + Constants.PREFIX +
                                   "shop {item} confirm` if you're sure you want to buy this.\nWhen you buy this, " +
                                   "nothing will be reset, " + "but you will obtain {evolve_souls} " +
                                   EmoteUtil.getSoul(), Arrays.asList("{item}", "{evolve_souls}")) {
            @Override
            public String format(Player player, String placeholder, String string) {
                if (placeholder.equalsIgnoreCase("{item}")) return string.replace(placeholder, getDisplayName());
                if (placeholder.equalsIgnoreCase("{evolve_souls}"))
                    return string.replace(placeholder, "`" + player.getObtainableSouls() + "`");
                return string;
            }
        };
    }
}