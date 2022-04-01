package org.affluentproductions.idlepokemon.manager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.util.FormatUtil;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class BanManager {

    public static void checkBans() {
        try {
            if (IdlePokemon.test) return;
            ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM bans;");
            while (rs.next()) isBanned(rs.getString("userId"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void unban(String uid) {
        IdlePokemon.getBot().getDatabase().update("DELETE FROM bans WHERE userId=?;", uid);
    }

    public static boolean isBanned(String uid) {
        if (IdlePokemon.test) return false;
        try {
            ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM bans WHERE userId=?;", uid);
            if (rs.next()) {
                boolean banned = FormatUtil.getNow()
                        .before(Objects.requireNonNull(FormatUtil.fromString(rs.getString("todate"))));
                if (!banned) IdlePokemon.getBot().getDatabase().update("DELETE FROM bans WHERE userId=?;", uid);
                return banned;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void ban(String uid, Date when, String reason, String by) {
        String name = "User not found#0000 - null";
        User user = IdlePokemon.getBot().getShardManager().getUserById(uid);
        if (user != null) name = user.getName() + "#" + user.getDiscriminator();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Banned user");
        eb.setColor(Color.RED);
        eb.addField("User ID", uid, true);
        eb.addField("Name", name, true);
        eb.addField("Banned by", by, true);
        eb.addField("Time", "From: " + FormatUtil.fromDate(FormatUtil.getNow()) + "\nTo: " + FormatUtil.fromDate(when),
                true);
        eb.addField("Reason", reason, true);
        IdlePokemon.getModLog().sendMessage(eb.build()).queue();
        IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO bans VALUES ('" + uid + "', '" + FormatUtil.fromDate(when) + "');");
    }
}