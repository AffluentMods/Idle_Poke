package org.affluentproductions.idlepokemon.achievements;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.event.pokemon.PokemonUpgradeEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BuyUpgradesAchievement extends Achievement {

    private static final List<Long> values = Arrays.asList(25L, 50L, 100L, 200L);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (long value : values)
            achievementData.add(new AchievementData("Buy " + FormatUtil.formatCommas(value) + " pokémon upgrades"));
    }

    @Override
    long getSR() {
        return 5;
    }

    @Override
    long getIR() {
        return 10;
    }

    public BuyUpgradesAchievement() {
        super("Buy Upgrades", achievementData);
    }

    @Override
    public void onPokemonUpgradeEvent(PokemonUpgradeEvent e) {
        String uid = e.getUserId();
        long upgrades = Long.parseLong(StatsUtil.getStat(uid, "pokemon-upgrades", "0"));
        long newUpgrades = upgrades + 1;
        StatsUtil.setStat(uid, "pokemon-upgrades", newUpgrades + "");
        //
        Player p = new Player(uid);
        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (long value : values) if (upgrades >= value) newTier++;
        if (newTier > achievedTier) {
            Achievements.addAchievement(uid, this, newTier);
            long rr = getReward(newTier);
            p.getEcoUser().addRubies(BigInteger.valueOf(rr));
            MessageEmbed msg = MessageUtil.info("Achievement",
                    "You just achieved " + getName() + " " + newTier + "!\n**+ " + EmoteUtil.getRuby() + " `x" +
                    FormatUtil.formatCommas(rr) + "`**");
            try {
                p.getUser().openPrivateChannel().queue(pc -> pc.sendMessage(msg).queue());
            } catch (Exception ignored) {
            }
        }
    }
}