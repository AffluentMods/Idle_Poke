package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.entity.Player;

public class ClangorousSoul extends Skill {

    private static final int pokemonID = 18;
    private static final int upgradeID = 4;

    public ClangorousSoul() {
        super(6, "Clangorous Soul",
                "Multiply your current DPS by 1.05 for the duration of your world. Stacks up to 20 times per " +
                "Evolution.", new SkillEffect(-1) {
                    @Override
                    public boolean isProduct() {
                        return true;
                    }

                    @Override
                    public boolean canDoubleEffect() {
                        return false;
                    }

                    @Override
                    public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                        ClangorousSoul.run(player, isBuy, doubleEffect);
                    }

                    @Override
                    public void reactivate(Player player, int amount) {
                        ClangorousSoul.rerun(player, amount);
                    }

                    @Override
                    public void deactivate(Player player) {
                        ClangorousSoul.end(player);
                    }
                }, pokemonID, upgradeID, 75, 8 * 60 * 60 * 1000);
    }

    public static int getPokemonID() {
        return pokemonID;
    }

    public static int getUpgradeID() {
        return upgradeID;
    }

    private static void rerun(Player player, int times) {
        player.setProduct("Clangorous Soul", times);
        player.removeDPSMultiplier("Clangorous Soul");
        double multiplier = times * 0.05;
        player.addDPSMultiplier("Clangorous Soul", multiplier);
        player.updateDPS();
        player.updateCD();
    }

    public static void run(Player player, boolean isBuy, boolean doubleEffect) {
        int times = player.getProducts().getOrDefault("clangorous soul", 0);
        if (isBuy) times++;
        rerun(player, times);
    }

    public static void end(Player player) {
    }
}