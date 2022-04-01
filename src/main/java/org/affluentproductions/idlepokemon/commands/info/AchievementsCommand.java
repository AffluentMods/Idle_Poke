package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.achievements.Achievement;
import org.affluentproductions.idlepokemon.achievements.AchievementData;
import org.affluentproductions.idlepokemon.achievements.Achievements;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.util.HashMap;

public class AchievementsCommand extends BotCommand {

    public AchievementsCommand() {
        this.name = "achievements";
        this.cooldown = 1.25;

        this.aliases = new String[]{"achievement", "quest", "quests", "mission", "missions"};
    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        Player p = new Player(u.getId());
        HashMap<Achievement, Integer> userAchievements = Achievements.getAchievedAchievements(u.getId());
        HashMap<String, Achievement> achievementList = Achievement.getAllAchievements();
        StringBuilder response = new StringBuilder();
        for (Achievement achievement : achievementList.values()) {
            String name = achievement.getName();
            int tier = 0;
            if (userAchievements.containsKey(achievement)) tier = userAchievements.get(achievement);
            String tierDisplay = "";
            for (int i = 0; i < achievement.getAchievementDataMap().size(); i++) {
                if (i < tier) tierDisplay += EmoteUtil.getEmoteMention("S_") + " ";
                else tierDisplay += EmoteUtil.getEmoteMention("E_S") + " ";
            }
            System.out.println(name + " | " + tier + "/" + achievement.getAchievementDataMap().size());
            final int index = tier;
            AchievementData ad = achievement.getAchievementData(index + 1);
            String desc;
            if (tier == achievement.getAchievementDataMap().size()) {
                desc = "Completed";
                response.append("**").append(name).append("**\n").append(tierDisplay).append("\n").append(desc)
                        .append("\n\n");
            } else {
                desc = ad.getDescription();
                long rr = achievement.getReward(tier + 1);
                response.append("**").append(name).append("**\n").append(tierDisplay).append("\n").append(desc)
                        .append("\n- Reward: ").append(EmoteUtil.getRuby()).append(" ")
                        .append(FormatUtil.formatCommas(rr)).append("\n\n");
            }
        }
        System.out.println(response.toString().length());
        e.reply(MessageUtil.info("Achievements of " + p.getUser().getAsTag(), response.toString()));
    }
}