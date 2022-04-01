package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.achievements.Achievement;
import org.affluentproductions.idlepokemon.ancient.Ancients;
import org.affluentproductions.idlepokemon.botlistener.BotInviteListener;
import org.affluentproductions.idlepokemon.botlistener.CommandListener;
import org.affluentproductions.idlepokemon.botlistener.DonatorRewardsListener;
import org.affluentproductions.idlepokemon.botlistener.FeedbackListener;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.item.Item;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.vote.VoteSystem;

public class LoadUtil {

    public static void load() {
        ShinyUtil.load();
        CooldownUtil.loadCooldowns();
        Pokemon.loadPokemons();
        GuideUtil.loadGuides();
        VoteSystem.load();
        Skill.load();
        Item.load();
        Achievement.loadAchievements();
        //
        Player.init();
        Ancients.load();
        CommandListener.load();
        IdlePokemon.getBot().getShardManager()
                .addEventListener(new FeedbackListener(), new BotInviteListener(), new DonatorRewardsListener());
        ServerUtil.load();
        //
        System.out.println("Loading Clicker...");
        ClickUtil.loadClicker();
    }

}