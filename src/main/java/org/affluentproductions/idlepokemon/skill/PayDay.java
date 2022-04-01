package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

import java.util.HashMap;

public class PayDay extends Skill {

    private static final int pokemonID = 16;
    private static final int upgradeID = 5;

    public static final HashMap<String, Double> multiplier = new HashMap<>();

    public PayDay() {
        super(5, "Pay Day", "1% of the rival's gold, or 0.1% of the boss's gold is gained per click for 30 seconds",
                new SkillEffect(30 * 1000) {
                    @Override
                    public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                        PayDay.run(player, isBuy, doubleEffect);
                    }

                    @Override
                    public void deactivate(Player player) {
                        PayDay.end(player);
                    }
                }, pokemonID, upgradeID, 100, 60 * 60 * 1000);
    }

    public static int getPokemonID() {
        return pokemonID;
    }

    public static int getUpgradeID() {
        return upgradeID;
    }

    public static void run(Player player, boolean isBuy, boolean doubleEffect) {
        double val = 0.01;
        if (doubleEffect) val = val * 2;
        player.addBonus("Pay Day", new Bonus(val, BonusType.GOLD_WORTH));
    }

    public static void end(Player player) {
        player.removeBonus("Pay Day");
    }
}