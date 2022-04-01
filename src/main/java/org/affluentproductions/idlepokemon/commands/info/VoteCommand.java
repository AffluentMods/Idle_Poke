package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.vote.VoteClient;
import org.affluentproductions.idlepokemon.vote.VoteSystem;

import java.util.List;

public class VoteCommand extends BotCommand {

    public static String voteLink = "https://top.gg/bot/670216386827780106";

    public VoteCommand() {
        this.name = "vote";

        this.aliases = new String[]{"v"};
        this.cooldown = 1;
    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length > 0) {
            String arg = args[0].toLowerCase();
            if (arg.equals("check")) {
                if (VoteSystem.hasVoted(uid)) {
                    String active_vote = "Active Vote";
                    String active_vote_msg = "You already have an active vote boost!";
                    e.reply(MessageUtil.err(active_vote, active_vote_msg));
                    return;
                }
                if (IdlePokemon.dblapi != null) {
                    IdlePokemon.dblapi.hasVoted(uid).whenCompleteAsync((voted, error) -> {
                        if (error != null) {
                            e.reply(MessageUtil.err("Error", "Could not retrieve data"));
                            return;
                        }
                        if (voted) {
                            VoteClient.vote(uid, true);
                        } else {
                            e.reply(MessageUtil.err("Vote", "You didn't vote yet!"));
                        }
                    });
                }
                return;
            }
        }
        if (VoteSystem.hasVoted(u.getId())) {
            e.reply(MessageUtil.err("Active Vote", "You already have an active vote boost!"));
        }
        StringBuilder voteRewards = new StringBuilder();
        List<String> voteRewardsList = VoteClient.getVoteRewards(VoteClient.lastWeekend);
        for (String voteReward : voteRewardsList) {
            String item = voteReward.split(":")[0];
            String amount = voteReward.split(":")[1];
            if (item.equalsIgnoreCase("coins_stock")) {
                voteRewards.append(EmoteUtil.getCoin()).append(" `").append(amount).append("%")
                        .append("` of your current coins").append("\n");
                continue;
            }
            if (item.equalsIgnoreCase("coin_gain_bonus")) {
                voteRewards.append(EmoteUtil.getCoin()).append(" `").append(amount).append("%` Coin Gain\n");
                continue;
            }
            if (item.equalsIgnoreCase("dps_boost")) {
                voteRewards.append("`").append(amount).append("%` DPS Boost\n");
                continue;
            }
            voteRewards.append(EmoteUtil.getEmoteMention(item)).append(" ").append(item).append(" `x")
                    .append(FormatUtil.formatAbbreviated(amount)).append("`\n");
        }
        String maybeWeekend = VoteClient.lastWeekend ? " (+ Weekend Bonus)" : "";
        e.reply(MessageUtil.success("Vote link",
                "Click here to vote and get amazing rewards:\n" + voteLink + "\n\nYou didn't receive your rewards? " +
                "Type `" + Constants.PREFIX + "vote check` to get them!\n\nYour vote rewards" + maybeWeekend + ":\n" +
                voteRewards));
    }
}