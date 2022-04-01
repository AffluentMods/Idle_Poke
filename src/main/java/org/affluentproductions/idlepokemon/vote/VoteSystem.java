package org.affluentproductions.idlepokemon.vote;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.commands.info.VoteCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VoteSystem {

    private static final HashMap<String, Long> activeVotes = new HashMap<>();

    public static void load() {
        VoteServer.start();
        long now = System.currentTimeMillis();
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM votes;")) {
            while (rs.next()) {
                String uid = rs.getString("userId");
                long until = rs.getLong("until");
                if (until <= now) {
                    expireVote(uid);
                } else {
                    addVote(uid, new Player(uid), until, VoteClient.lastWeekend, false);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void addVote(String uid, Player p, long until, boolean weekend, boolean sql) {
        List<String> rewards = VoteClient.getVoteRewards(weekend);
        for (String rewardStr : rewards) {
            String reward = rewardStr.split(":")[0];
            long amount = Long.parseLong(rewardStr.split(":")[1]);
            if (reward.equalsIgnoreCase("coin_gain_bonus")) {
                p.addBonus("Vote Coin Gain", new Bonus((amount / 100.0), BonusType.GOLD_DROP));
                continue;
            }
            if (reward.equalsIgnoreCase("dps_boost")) {
                p.addDPSMultiplier("Vote DPS Boost", (amount / 100.0));
                continue;
            }
        }
        activeVotes.put(uid, until);
        if (sql) {
            IdlePokemon.getBot().getDatabase().update("DELETE FROM votes WHERE userId=?;", uid);
            IdlePokemon.getBot().getDatabase().update("INSERT INTO votes VALUES (?, ?);", uid, until);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activeVotes.remove(uid, until);
                expireVote(uid);
            }
        }, until - System.currentTimeMillis());
    }

    public static boolean hasVoted(String uid) {
        return activeVotes.getOrDefault(uid, -1L) > System.currentTimeMillis();
    }

    public static long getUntilVote(String uid) {
        return activeVotes.getOrDefault(uid, -1L);
    }

    private static void expireVote(String userId) {
        activeVotes.remove(userId);
        IdlePokemon.getBot().getDatabase().update("DELETE FROM votes WHERE userId=?;", userId);
        Player p = new Player(userId);
        p.removeDPSMultiplier("Vote DPS Boost");
        p.removeBonus("Vote Coin Gain");
        User u = IdlePokemon.getBot().getShardManager().getUserById(userId);
        if (u == null) return;
        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.err("Vote",
                "Your vote boost expired!\nYou can vote again [here](" + VoteCommand.voteLink + " \"Vote Link\")."))
                .queue());
    }
}