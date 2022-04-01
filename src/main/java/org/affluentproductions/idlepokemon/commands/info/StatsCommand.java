package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MentionUtil;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigInteger;

public class StatsCommand extends BotCommand {

    public StatsCommand() {
        this.name = "stats";
        this.cooldown = 0.5;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        User mentioned = MentionUtil.getUser(e.getMessage());
        if (mentioned != null) {
            u = mentioned;
        }
        String uid = u.getId();
        BigInteger coinGain = new BigInteger(StatsUtil.getStat(uid, "coins-gained", "0"));
        BigInteger rubyGain = new BigInteger(StatsUtil.getStat(uid, "rubies-gained", "0"));
        BigInteger soulGain = new BigInteger(StatsUtil.getStat(uid, "souls-obtained", "0"));
        BigInteger pokemonsLeveled = new BigInteger(StatsUtil.getStat(uid, "pokemon-leveled", "0"));
        long pokemonsUpgrades = Long.parseLong(StatsUtil.getStat(uid, "pokemon-upgrades", "0"));
        long clicks = Long.parseLong(StatsUtil.getStat(uid, "total-clicks", "0"));
        BigInteger skillUsed = new BigInteger(StatsUtil.getStat(uid, "skill-used", "0"));
        int evolutions = new Player(uid).getEvolutions();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Stats of " + u.getAsTag());
        eb.addField("Clicks", FormatUtil.formatCommas(clicks), true);
        eb.addField("Coins gained", FormatUtil.formatAbbreviated(coinGain), true);
        eb.addField("Rubies gained", FormatUtil.formatCommas(rubyGain), true);
        eb.addField("Souls obtained", FormatUtil.formatCommas(soulGain), true);
        eb.addField("Pokémon's leveled", FormatUtil.formatAbbreviated(pokemonsLeveled), true);
        eb.addField("Upgrades bought", FormatUtil.formatCommas(pokemonsUpgrades), true);
        eb.addField("Skills used", FormatUtil.formatCommas(skillUsed), true);
        eb.addField("Evolutions", FormatUtil.formatCommas(evolutions), true);
        e.reply(eb.build());
    }
}