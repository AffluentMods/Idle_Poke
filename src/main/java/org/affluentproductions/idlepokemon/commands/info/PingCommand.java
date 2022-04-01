package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.db.Database;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.ClickUtil;
import org.affluentproductions.idlepokemon.util.CooldownUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

public class PingCommand extends BotCommand {

    public PingCommand() {
        this.name = "ping";
        this.cooldown = 1;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String p1 = String.valueOf(IdlePokemon.getBot().getShardManager().getAverageGatewayPing());
        if (p1.length() > 6) p1 = p1.substring(0, 6);
        String p2 = String.valueOf(Database.ping);
        if (p2.length() > 6) p2 = p2.substring(0, 6);
        String p3 = String.valueOf(e.getJDA().getGatewayPing());
        if (p3.length() > 6) p3 = p3.substring(0, 6);
        long cp = ClickUtil.clickPing;
        String cpstatus;
        double cps = cp / 1000.0;
        if (cps < 2.9) cpstatus = "Too fast/too good??";
        else if (cps < 4) cpstatus = "Perfect!";
        else if (cps < 7) cpstatus = "Bad!";
        else if (cps < 10) cpstatus = "Very Bad!!";
        else cpstatus = "Terrible!!!";
        String clickping = cps + "s Timer Re-Run (" + cpstatus + ")";
        long restartAt = IdlePokemon.restartAt;
        long now = System.currentTimeMillis();
        long diff = restartAt - now;
        String ras = diff > 0 ? "in " + CooldownUtil.format(diff, u.getId()) + "." : "now.";
        e.reply(MessageUtil.info("Ping", u.getAsMention() + " » The bot's average gateway ping is " + p1 +
                                         "ms\nThe latest database request took " + p2 + "ms\nThis shard's (#" +
                                         e.getJDA().getShardInfo().getShardId() + ") gateway ping is " + p3 +
                                         "ms\nDPS Re-Run: " + clickping + "\nThe bot will restart " + ras));
    }
}