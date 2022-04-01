package org.affluentproductions.idlepokemon.achievements;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.event.economy.EconomyEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HoardingAchievement extends Achievement {

    private static final BigInteger v1 = BigDecimal.valueOf(5e5).toBigInteger();
    private static final BigInteger v2 = BigDecimal.valueOf(5e12).toBigInteger();
    private static final BigInteger v3 = BigDecimal.valueOf(5e20).toBigInteger();
    private static final BigInteger v4 = BigDecimal.valueOf(5e31).toBigInteger();
    private static final List<BigInteger> values = Arrays.asList(v1, v2, v3, v4);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (BigInteger value : values)
            achievementData.add(new AchievementData("Hoard " + FormatUtil.formatAbbreviated(value) + " coins"));
    }

    @Override
    long getSR() {
        return 5;
    }

    @Override
    long getIR() {
        return 10;
    }

    public HoardingAchievement() {
        super("Hoarding", achievementData);
    }

    @Override
    public void onEconomyEvent(EconomyEvent event) {
        String uid = event.getUserId();
        Player p = new Player(uid);
        //
        BigInteger v = event.getEcoUser().getCoins();
        //
        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (BigInteger value : values) if (v.compareTo(value) > 0) newTier++;
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