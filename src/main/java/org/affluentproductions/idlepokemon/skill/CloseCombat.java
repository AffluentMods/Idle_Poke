package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Player;

public class CloseCombat extends Skill {

    private static final int pokemonID = 21;
    private static final int upgradeID = 5;

    public CloseCombat() {
        super(7, "Close Combat", "+200% Click Damage for 30 seconds.", new SkillEffect(30 * 1000) {
            @Override
            public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                CloseCombat.run(player, isBuy, doubleEffect);
            }

            @Override
            public void deactivate(Player player) {
                CloseCombat.end(player);
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
        double val = 2;
        if (doubleEffect) val = val * 2;
        player.addBonus("Close Combat", new Bonus(val, BonusType.CLICK_DAMAGE));
        player.updateDPS();
        player.updateCD();
    }

    public static void end(Player player) {
        player.removeBonus("Close Combat");
        player.updateDPS();
        player.updateCD();
    }
}