package org.affluentproductions.idlepokemon.botlistener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.entity.EcoUser;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DonatorRewardsListener extends ListenerAdapter {

    public DonatorRewardsListener() {
        Guild g = IdlePokemon.getHub();
        if (g != null) {
            List<Role> bundleRoles = new ArrayList<>();
            for (Role role : g.getRoles()) {
                if (role.getName().toLowerCase().startsWith("ruby bundle ")) bundleRoles.add(role);
            }
            for (Role bundleRole : bundleRoles) {
                String[] splitted = bundleRole.getName().toLowerCase().split(" ");
                int bundleID = Integer.parseInt(splitted[splitted.length - 1]);
                List<Member> members = g.getMembersWithRoles(bundleRole);
                for (Member m : members) {
                    doRewardThing(m.getGuild(), m, bundleRole, new Player(m.getId(), false), m.getUser(), bundleID);
                }
            }
        }
    }

    @Override
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
        if (!event.getGuild().getId().equals(Constants.main_guild)) return;
        List<Role> roles = event.getRoles();
        int bundleID = 0;
        long roleID = -1;
        Role finalRole = null;
        for (Role role : roles) {
            if (role.getName().toLowerCase().startsWith("ruby bundle")) {
                String[] splitted = role.getName().split(" ");
                bundleID = Integer.parseInt(splitted[splitted.length - 1]);
                roleID = role.getIdLong();
                finalRole = role;
                break;
            }
        }
        if (bundleID <= 0 || roleID == -1) {
            return;
        }
        User u = event.getMember().getUser();
        Player p = new Player(u.getId(), false);
        doRewardThing(event.getGuild(), event.getMember(), finalRole, p, u, bundleID);
    }

    private void doRewardThing(Guild g, Member m, Role finalRole, Player p, User u, int bundleID) {
        g.removeRoleFromMember(m, finalRole).queue();
        HashMap<String, String> rewards = new HashMap<>();
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM donatorRoleRewards WHERE bundle=?;", bundleID)) {
            while (rs.next()) {
                rewards.put(rs.getString("rw_item"), rs.getString("rw_amount"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        EcoUser ecoUser = p.getEcoUser();
        StringBuilder rewardsString = new StringBuilder();
        for (String reward : rewards.keySet()) {
            BigInteger amount = new BigInteger(rewards.get(reward));
            if (reward.equalsIgnoreCase("ruby")) ecoUser.addRubies(amount);
            rewardsString.append("- ").append(EmoteUtil.getEmoteMention(reward)).append(" ").append(reward)
                    .append(" `x").append(FormatUtil.formatCommas(amount)).append("`\n");
        }
        IdlePokemon.getModLog().sendMessage(MessageUtil.info("Ruby Bundle " + bundleID + " given",
                u.getAsMention() + " `" + u.getAsTag() + "` received Ruby Bundle " + bundleID +
                " and the following rewards:\n\n" + rewardsString.toString())).queue();
        if (!p.getBadges().contains("donator")) p.addBadge("donator");
    }
}