package org.affluentproductions.idlepokemon.commands.util;

import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrizeCommand extends BotCommand {

    public PrizeCommand() {
        this.name = "prize";
        this.aliases = new String[]{"compensation"};
        this.cooldown = 1;
        if (oldTableName != null) IdlePokemon.getBot().getDatabase().update("DROP TABLE " + oldTableName + ";");
        IdlePokemon.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS " + tableName + " (userId VARCHAR(24) NOT NULL);");
    }

    // compensationPrize_day_month_year
    private static final String oldTableName = null;
    private static final String tableName = "compensationPrize_02_05_20";
    public static final boolean enabled = true;

    @Override
    public void execute(CommandEvent e) {
        if (!enabled) {
            e.reply(MessageUtil.err("Disabled", "There is no active prize available."));
            return;
        }
        String uid = e.getAuthor().getId();
        Player p = new Player(uid);
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM " + tableName + " WHERE userId='" + uid + "';")) {
            if (rs.next()) {
                e.reply(MessageUtil.err("Error", "You already received your prize!"));
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            e.reply(MessageUtil.err("Error", "A system error occurred. Please contact the support."));
            return;
        }
        IdlePokemon.getBot().getDatabase().update("INSERT INTO " + tableName + " VALUES ('" + uid + "');");
        long rubyReward = 100;
        p.getEcoUser().addRubies(BigInteger.valueOf(rubyReward));
        e.reply(MessageUtil.success("Prize", "Here is your prize:\n" + EmoteUtil.getRuby() + " `x" + rubyReward + "`"));
    }
}