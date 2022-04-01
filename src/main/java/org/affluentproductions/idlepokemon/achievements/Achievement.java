package org.affluentproductions.idlepokemon.achievements;

import org.affluentproductions.idlepokemon.listener.EventListener;
import org.affluentproductions.idlepokemon.manager.EventManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Achievement implements EventListener {

    private final String name;
    private final HashMap<Integer, AchievementData> achievementDataMap;
    private static final HashMap<String, Achievement> allAchievements = new HashMap<>();

    public Achievement(final String name, final List<AchievementData> achievementDatas) {
        this.name = name;
        HashMap<Integer, AchievementData> achievementDataMap = new HashMap<>();
        int dataTier = 0;
        for (final AchievementData achievementData : achievementDatas) {
            dataTier++;
            achievementDataMap.put(dataTier, achievementData);
        }
        this.achievementDataMap = achievementDataMap;
    }

    abstract long getSR();

    abstract long getIR();

    public long getReward(int times) {
        if (times < 2) return getSR();
        return getSR() + (getIR() * (times - 1));
    }

    public static Achievement getAchievementByName(String achievementName) {
        return allAchievements.get(achievementName.toLowerCase());
    }

    public static void loadAchievements() {
        List<Achievement> achievements =
                Arrays.asList(new HoardingAchievement(), new TotalGoldAchievement(), new PokemonLevelUpAchievement(),
                        new BuyUpgradesAchievement(), new TotalClicksAchievement(), new HighestZoneAchievement());
        for (Achievement achievement : achievements) {
            allAchievements.put(achievement.getName().toLowerCase(), achievement);
            EventManager.addListener(achievement);
        }
    }

    public String getName() {
        return name;
    }

    public static HashMap<String, Achievement> getAllAchievements() {
        return allAchievements;
    }

    public HashMap<Integer, AchievementData> getAchievementDataMap() {
        return achievementDataMap;
    }

    public AchievementData getAchievementData(int index) {
        return achievementDataMap.get(index);
    }
}