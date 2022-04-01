package org.affluentproductions.idlepokemon.item;

import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

public class TimeLapse4 extends Item {

    public TimeLapse4() {
        super("Time Lapse 4", 500, "Instantly get 168 hours of progress.", false, false, 100000,
                new Bonus(0, BonusType.NULL), new ItemAction() {
                    @Override
                    public void run(Player player, TextChannel tc) {
                        TimeLapse1.doTimeLapse(player, 168);
                    }
                });
    }

}