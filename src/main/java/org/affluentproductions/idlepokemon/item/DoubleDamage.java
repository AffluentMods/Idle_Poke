package org.affluentproductions.idlepokemon.item;

import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

public class DoubleDamage extends Item {
    private static final Bonus bonus = new Bonus(1.0, BonusType.FULL_DAMAGE);

    public DoubleDamage() {
        super("x2 Damage", 500, "Adds a permanent x2 multiplier to all damage.", true, true, 1, bonus,
                new ItemAction() {
                    @Override
                    public void run(Player player, TextChannel tc) {
                        player.addBonus("x2 damage", bonus);
                        player.updateDPS();
                        player.updateCD();
                    }
                });
    }
}