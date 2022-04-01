package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.MentionUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.vote.VoteSystem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class BoostsCommand extends BotCommand {

    public BoostsCommand() {
        this.name = "boosts";
        this.aliases = new String[]{"bonuses"};
        this.cooldown = 2.5;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        Player p;
        User mentioned = MentionUtil.getUser(e.getMessage());
        if (mentioned != null) p = new Player(mentioned.getId());
        else p = new Player(uid);
        HashMap<String, Bonus> bonuses = p.getBonuses();
        HashMap<String, Integer> products = p.getProducts();
        HashMap<String, Double> dpsmps = p.getDpsMultiplier();
        int evolutions = p.getEvolutions();
        final String t = Constants.TAB;
        String boosts = "Evolutions: `" + evolutions + "`\n";
        List<String> activeSkills = p.getActiveSkills();
        if (activeSkills.size() > 0) {
            boosts += "\n• **Active Skills**\n";
            for (String ssid : activeSkills) {
                Skill skill = Skill.getSkill(Integer.parseInt(ssid));
                boosts += t + skill.getName() + "\n";
            }
        }
        boosts += "\n• **Bonuses**\n";
        for (String bonusName : bonuses.keySet()) {
            Bonus bonus = bonuses.get(bonusName);
            if (bonusName.toLowerCase().contains("ancient")) continue;
            else boosts +=
                    t + bonusName.toUpperCase() + ": " + bonus.getBonusType().name() + " `x" + bonus.getValue() + "`\n";
        }
        boosts += "\n• **Items bought**\n";
        for (String product : products.keySet()) {
            int amount = products.get(product);
            boosts += t + "`" + amount + "x` " + product + "\n";
        }
        boosts += "\n• **Multipliers**\n";
        double crit = p.getCritMultiplier();
        double dpsmp = 0;
        for (double dpsmp1 : dpsmps.values()) dpsmp = dpsmp1;
        String dpsmpd = String.valueOf(dpsmp);
        if (dpsmpd.length() > 7) dpsmpd = dpsmpd.substring(0, 7);
        double soulsMP = new BigDecimal(p.getEcoUser().getSouls()).multiply(BigDecimal.valueOf(0.1)).doubleValue();
        HashMap<String, Double> soulsdpsmps = p.getSoulDpsMultiplier();
        for (double souldpsmp1 : soulsdpsmps.values()) soulsMP += souldpsmp1;
        String soulsmpd = String.valueOf(soulsMP);
        if (soulsmpd.length() > 7) soulsmpd = soulsmpd.substring(0, 7);
        String voteAppend = VoteSystem.hasVoted(uid) ? " + Vote Boost" : "";
        boosts += t + "DPS: `x" + dpsmpd + "` | + Souls Boost `x" + soulsmpd + "`" + voteAppend + "\n";
        boosts += t + "Critical Damage: `x" + crit + "`\n";
        boosts += t + "Critical Chance: `" + p.getCritChance() + "%`\n";
        e.reply(MessageUtil.info("Boosts of " + p.getAsTag(), boosts));
    }
}