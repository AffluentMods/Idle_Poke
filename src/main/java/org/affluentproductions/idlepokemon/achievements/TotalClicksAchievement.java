package org.affluentproductions.idlepokemon.achievements;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.event.action.ClickEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TotalClicksAchievement extends Achievement {

    private static final List<Long> values = Arrays.asList(1000L, 10000L, 50000L, 100000L, 250000L);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (long value : values)
            achievementData.add(new AchievementData("Click " + FormatUtil.formatCommas(value) + " times"));
    }

    @Override
    long getSR() {
        return 5;
    }

    @Override
    long getIR() {
        return 10;
    }

    public TotalClicksAchievement() {
        super("Total Clicks", achievementData);
    }

    @Override
    public void onClickEvent(ClickEvent event) {
        String uid = event.getUserId();
        Player p = new Player(uid);
        //
        long clicks = Long.parseLong(StatsUtil.getStat(uid, "total-clicks", "0"));
        long newClicks = clicks + 1;
        StatsUtil.setStat(uid, "total-clicks", newClicks + "");
        //
        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (long value : values) if (newClicks >= value) newTier++;
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