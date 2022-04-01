package org.affluentproductions.idlepokemon.item;

import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.ClickUtil;

import java.math.BigDecimal;

public class TimeLapse1 extends Item {

    public TimeLapse1() {
        super("Time Lapse 1", 100, "Instantly get 8 hours of progress.", false, false, 100000,
                new Bonus(0, BonusType.NULL), new ItemAction() {
                    @Override
                    public void run(Player player, TextChannel tc) {
                        doTimeLapse(player, 8);
                    }
                });
    }

    public static void doTimeLapse(Player player, int hours) {
        BigDecimal dps = player.getDPS().multiply(BigDecimal.valueOf(hours * 60 * 60));
        final String uid = player.getUserId();
        ClickUtil.preventClick(uid, 30000);
        ClickUtil.doFullClick(player, dps, 4475);
        ClickUtil.preventClick(uid, -1);
    }
}