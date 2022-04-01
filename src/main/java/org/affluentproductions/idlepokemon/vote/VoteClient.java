package org.affluentproductions.idlepokemon.vote;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VoteClient {

    public static boolean lastWeekend = false;
    private static final List<String> voteQueue = new ArrayList<>();

    VoteClient(final Socket socket, final InputStream in, final long id) {
        final DataInputStream dis = new DataInputStream(in);
        try {
            boolean authorized = false;
            while (socket != null && !socket.isClosed()) {
                @SuppressWarnings("deprecation") final String line = dis.readLine();
                if (line == null) continue;
                if (line.startsWith("Authorization: ")) {
                    if (line.substring("Authorization: ".length()).equals(Constants.dbl_vote_auth)) {
                        authorized = true;
                    }
                }
                if (line.startsWith("{")) {
                    final JSONObject json = new JSONObject(line);
                    final String uid = json.getString("user");
                    final boolean weekend = json.getBoolean("isWeekend");
                    lastWeekend = weekend;
                    vote(uid, weekend, authorized);
                    //
                    dis.close();
                    socket.close();
                    VoteServer.close(id);
                    break;
                }
            }
        } catch (SocketException | SocketTimeoutException ignored) {
            VoteServer.close(id);
        } catch (Exception ex) {
            VoteServer.close(id);
            ex.printStackTrace();
        }
    }

    public static void vote(final String uid, final boolean authorized) {
        vote(uid, lastWeekend, authorized);
    }

    public static void vote(final String uid, final boolean weekend, final boolean authorized) {
        if (!authorized) {
            return;
        }
        final User u = IdlePokemon.getBot().getShardManager().getUserById(uid);
        if (u != null) {
            if (voteQueue.contains(uid)) return;
            if (!VoteSystem.hasVoted(uid)) {
                voteQueue.add(uid);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        voteQueue.remove(uid);
                    }
                }, 7544);
                final Player p = new Player(uid);
                final EcoUser ecoUser = p.getEcoUser();
                List<String> rewards = getVoteRewards(weekend);
                StringBuilder rewardsDisplay = new StringBuilder();
                for (String rewardStr : rewards) {
                    String reward = rewardStr.split(":")[0];
                    long amount = Long.parseLong(rewardStr.split(":")[1]);
                    if (reward.equalsIgnoreCase("ruby")) {
                        ecoUser.addRubies(new BigInteger(amount + ""));
                    }
                    if (reward.equalsIgnoreCase("coins_stock")) {
                        BigInteger coinsReward =
                                new BigDecimal(ecoUser.getCoins()).multiply(BigDecimal.valueOf(amount / 100.0))
                                        .toBigInteger();
                        ecoUser.addCoins(coinsReward);
                        rewardsDisplay.append(EmoteUtil.getCoin()).append(" ").append("`")
                                .append(FormatUtil.formatAbbreviated(coinsReward)).append("` \n");
                        continue;
                    }
                    if (reward.equalsIgnoreCase("coin_gain_bonus")) {
                        // Will be added in VoteSystem.addVote(...)
                        //p.addBonus("Vote Coin Gain", new Bonus((amount / 100.0), BonusType.GOLD_DROP));
                        rewardsDisplay.append("**+ ").append(amount).append("% Coin Gain**\n");
                        continue;
                    }
                    if (reward.equalsIgnoreCase("dps_boost")) {
                        // Will be added in VoteSystem.addVote(...)
                        //p.addDPSMultiplier("Vote DPS Boost", (amount / 100.0));
                        rewardsDisplay.append("**+ ").append(amount).append("% DPS Bonus**\n");
                        continue;
                    }
                    rewardsDisplay.append(EmoteUtil.getEmoteMention(reward)).append(" ").append(reward).append(" `x")
                            .append(FormatUtil.formatCommas(amount)).append("`\n");
                }
                long now = System.currentTimeMillis();
                final long diff = 43200000L;
                long until = now + diff;
                final String finalRewardsDisplay = rewardsDisplay.toString();
                VoteSystem.addVote(u.getId(), p, until, weekend, true);
                String weekendVote = "Weekend Vote";
                String vote = "Vote";
                u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.success(weekend ? weekendVote : vote,
                        "Thank you for upvoting Idle Pokémon!\n\nHere are your rewards:\n" + finalRewardsDisplay))
                        .queue());
            }
        }
    }

    public static List<String> getVoteRewards(boolean weekend) {
        List<String> voteRewards = new ArrayList<>();
        int rubyAmount = weekend ? 10 : 5;
        voteRewards.add("ruby:" + rubyAmount);
        voteRewards.add("coins_stock:10");
        voteRewards.add("coin_gain_bonus:15");
        voteRewards.add("dps_boost:35");
        return voteRewards;
    }
}