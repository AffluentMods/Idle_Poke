package org.affluentproductions.idlepokemon.achievements;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.event.pokemon.PokemonLevelEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PokemonLevelUpAchievement extends Achievement {

    private static final List<Integer> values = Arrays.asList(100, 500, 2500, 10000, 20000, 50000);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (int value : values)
            achievementData.add(new AchievementData("Level Up pokémons " + FormatUtil.formatCommas(value) + " times"));
    }

    @Override
    long getSR() {
        return 5;
    }

    @Override
    long getIR() {
        return 5;
    }

    public PokemonLevelUpAchievement() {
        super("Pokémon Level Up", achievementData);
    }

    @Override
    public void onPokemonLevelEvent(PokemonLevelEvent e) {
        String uid = e.getUserId();
        int upgradeAmount = e.getNewLevel() - e.getOldLevel();
        BigInteger leveled = new BigInteger(StatsUtil.getStat(uid, "pokemon-leveled", "0"));
        BigInteger newLv = leveled.add(BigInteger.valueOf(upgradeAmount));
        StatsUtil.setStat(uid, "pokemon-leveled", newLv.toString());
        //
        Player p = new Player(uid);
        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (int value : values) if (newLv.compareTo(BigInteger.valueOf(value)) > 0) newTier++;
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