package org.affluentproductions.idlepokemon.achievements;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.event.action.ClickEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HighestZoneAchievement extends Achievement {

    private static final List<Integer> values =
            Arrays.asList(10, 25, 50, 100, 120, 140, 160, 1200, 1400, 1600, 1800, 2000, 2200, 2400, 2600, 2800, 3000,
                    3200, 3400, 3600);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (int value : values)
            achievementData.add(new AchievementData("Beat Zone " + FormatUtil.formatCommas(value)));
    }

    @Override
    long getSR() {
        return 5;
    }

    @Override
    long getIR() {
        return 10;
    }

    public HighestZoneAchievement() {
        super("Highest Zone", achievementData);
    }

    @Override
    public void onClickEvent(ClickEvent event) {
        String uid = event.getUserId();
        Player p = new Player(uid);
        int newZone = p.getRivalUser().getMaxStage();
        //
        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (long value : values) if (newZone >= value) newTier++;
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