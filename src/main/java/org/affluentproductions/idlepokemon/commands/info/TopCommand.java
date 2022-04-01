package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.EmoteUtil;
import org.affluentproductions.idlepokemon.util.FormatUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopCommand extends BotCommand {

    public TopCommand() {
        this.name = "top";

        this.cooldown = 2.5;
    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String[] args = e.getArgs();
        if (args.length == 0) {
            e.reply(MessageUtil
                    .info("Usage", "Please use `" + Constants.PREFIX + this.name + " <money | evolutions | stage>`"));
            return;
        }
        List<String> top = new ArrayList<>();
        String arg = args[0].toLowerCase();
        int page;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1 || page > 10) {
                    e.reply(MessageUtil.err("Error", "The argument `page` has to be between 1 and 10!"));
                    return;
                }
            } catch (NumberFormatException ex) {
                page = 1;
            }
        } else {
            page = 1;
        }
        int from = page * 10 - 9;
        int to = page * 10;
        boolean valid = false;
        if (arg.equalsIgnoreCase("money")) {
            valid = true;
            top = getTop(EmoteUtil.getCoin() + " {ABBR}{value}", from, to, "economy", "c", "c", true,
                    "SELECT *, CAST(coins AS DECIMAL(65)) AS c FROM economy ORDER BY c DESC LIMIT 500");
        }
        if (arg.equalsIgnoreCase("evolutions")) {
            valid = true;
            top = getTop("{value} Evolutions", from, to, "evolutions", "evolutions", true);
        }
        if (arg.equalsIgnoreCase("stage")) {
            valid = true;
            top = getTop("Stage {value}", from, to, "profiles", "stage", true);
        }
        if (!valid) {
            e.reply(MessageUtil.err("Error", "Invalid argument."));
            return;
        }
        String argDisplay = arg.substring(0, 1).toUpperCase() + arg.substring(1).toLowerCase();
        String topDisplay = "";
        for (String t : top) topDisplay += t + "\n";
        e.reply(MessageUtil.info("Top " + from + " - " + to + " | " + argDisplay, topDisplay));
    }

    private List<String> getTop(String formatVal, int offset, int to, String table, String column, boolean descend,
                                String sql) {
        return getTop(formatVal, offset, to, table, column, column, descend, sql);
    }

    private List<String> getTop(String formatVal, int offset, int to, String table, String column, boolean descend) {
        return getTop(formatVal, offset, to, table, column, column, descend, null);
    }

    private List<String> getTop(String formatVal, int offset, int to, String table, String queryColumn,
                                String fetchColumn, boolean descend, String sql) {
        List<String> tops = new ArrayList<>();
        int rank = 0;
        String desc = descend ? " DESC" : "";
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query(sql != null ? sql :
                "SELECT * FROM " + table + " ORDER BY " + queryColumn + desc + " LIMIT 500")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to) break;
                String userId = rs.getString("userId");
                User u = IdlePokemon.getBot().getShardManager().getUserById(userId);
                if (u == null || !Player.isPlayer(userId)) {
                    rank--;
                    continue;
                }
                Player t = new Player(userId, false);
                String tDisplay = t.isActive() ? t.getAsTag() : "~~*" + t.getAsTag() + "*~~";
                String value = String.valueOf(rs.getObject(fetchColumn));
                String val = formatVal.replace("{ABBR}{value}", FormatUtil.formatAbbreviated(value))
                        .replace("{value}", value);
                tops.add("**#" + rank + "** " + tDisplay + ": " + val + "");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tops;
    }
}