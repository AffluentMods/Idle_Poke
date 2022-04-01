package org.affluentproductions.idlepokemon.achievements;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.event.economy.EconomyEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TotalGoldAchievement extends Achievement {

    private static final BigInteger v1 = BigDecimal.valueOf(1e6).toBigInteger();
    private static final BigInteger v2 = BigDecimal.valueOf(1e9).toBigInteger();
    private static final BigInteger v3 = BigDecimal.valueOf(1e15).toBigInteger();
    private static final BigInteger v4 = BigDecimal.valueOf(1e20).toBigInteger();
    private static final BigInteger v5 = BigDecimal.valueOf(1e26).toBigInteger();
    private static final BigInteger v6 = BigDecimal.valueOf(1e32).toBigInteger();
    private static final BigInteger v7 = BigDecimal.valueOf(1e38).toBigInteger();
    private static final BigInteger v8 = BigDecimal.valueOf(1e44).toBigInteger();
    private static final BigInteger v9 = BigDecimal.valueOf(1e50).toBigInteger();
    private static final BigInteger v10 = BigDecimal.valueOf(1e56).toBigInteger();
    private static final BigInteger v11 = BigDecimal.valueOf(1e62).toBigInteger();
    private static final BigInteger v12 = BigDecimal.valueOf(1e68).toBigInteger();
    private static final BigInteger v13 = BigDecimal.valueOf(1e74).toBigInteger();
    private static final BigInteger v14 = BigDecimal.valueOf(1e197).toBigInteger();
    private static final List<BigInteger> values =
            Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (BigInteger value : values)
            achievementData.add(new AchievementData("Receive " + FormatUtil.formatAbbreviated(value) + " total coins"));
    }

    @Override
    long getSR() {
        return 5;
    }

    @Override
    long getIR() {
        return 5;
    }

    public TotalGoldAchievement() {
        super("Total Gold", achievementData);
    }

    @Override
    public void onEconomyEvent(EconomyEvent event) {
        String uid = event.getUserId();
        Player p = new Player(uid);
        BigInteger ob = event.getOldBalance();
        BigInteger nb = event.getNewBalance();
        BigInteger added = nb.subtract(ob);
        if (BigInteger.ZERO.compareTo(added) > 0) return;
        //
        BigInteger coinsGained = new BigInteger(StatsUtil.getStat(uid, "coins-gained", "0"));
        BigInteger v = coinsGained.add(added);
        StatsUtil.setStat(uid, "coins-gained", v.toString());
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