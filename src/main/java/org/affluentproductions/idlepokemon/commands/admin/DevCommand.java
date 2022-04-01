package org.affluentproductions.idlepokemon.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.botlistener.CommandListener;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.commands.action.ClickCommand;
import org.affluentproductions.idlepokemon.db.Database;
import org.affluentproductions.idlepokemon.entity.*;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.*;

import java.lang.management.*;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.TreeMap;

public class DevCommand extends BotCommand {

    public DevCommand() {
        this.name = "dev";
        this.cooldown = 0.5;
        this.ownerCommand = true;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length == 0) {
            e.reply(MessageUtil
                    .info("Usage", "Please use `" + Constants.PREFIX + this.name + " <sql | stats | ...>`" + "."));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equals("dumpstack")) {
            TreeMap<Long, Thread> cpus = new TreeMap<>();
            ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
            ThreadGroup tg = Thread.currentThread().getThreadGroup();
            for (Thread thread : Thread.getAllStackTraces().keySet()) {
                cpus.put(tmxb.getThreadCpuTime(thread.getId()), thread);
            }
            for (long cputime : cpus.descendingKeySet()) {
                Thread thread = cpus.get(cputime);
                System.out.println(tg.getName() + " | [" + thread.getId() + "] " + thread.getName() + " " +
                                   thread.getState().name() + " | " + cputime);
            }
            return;
        }
        if (arg.equals("pausedps")) {
            ClickUtil.pause = !ClickUtil.pause;
            e.reply("Click Timer Paused: " + ClickUtil.pause);
            return;
        }
        if (arg.equalsIgnoreCase("dpsdebug")) {
            ClickUtil.dpsdebug = ClickUtil.dpsdebug == null ? e.getMessage().getTextChannel().getId() : null;
            if (ClickUtil.dpsdebug == null) e.reply("Disabled DPS Debug");
            else e.reply("Enabled DPS Debug in this channel");
            return;
        }
        if (arg.equalsIgnoreCase("dpslog")) {
            ClickUtil.devlog = !ClickUtil.devlog;
            e.reply("ClickUtil.devlog = " + ClickUtil.devlog);
            return;
        }
        if (arg.equals("cmdlog")) {
            CommandListener.cmdlog = !CommandListener.cmdlog;
            e.reply("CommandListener.cmdlog = " + CommandListener.cmdlog);
            return;
        }
        if (arg.equals("log")) {
            ClickCommand.log = !ClickCommand.log;
            e.reply("ClickCommand.log = " + ClickCommand.log);
            return;
        }
        if (arg.equals("clearcache")) {
            Player.clearCache();
            RivalUser.clearCache();
            EcoUser.clearCache();
            PokemonUser.clearCache();
            EmoteUtil.clearCache();
            e.reply("Cache cleared.");
            return;
        }
        if (arg.equalsIgnoreCase("rlcd")) {
            CooldownUtil.loadCooldowns();
            e.reply("Cooldowns reloaded");
            return;
        }
        if (arg.equals("showpoks")) {
            String tid = args[1];
            Player t = new Player(tid);
            String a = "";
            for (Pokemon tp : t.getPokemons().values()) {
                a += tp.getID() + " Lv. " + tp.getLevel() + " " + tp.getDisplayName() + "\n ";
            }
            e.reply(a);
            return;
        }
        if (arg.equals("luc")) {
            BigInteger basecost = new BigInteger(args[1]);
            int level = Integer.parseInt(args[2]);
            int amount = Integer.parseInt(args[3]);
            e.reply("Level Up Cost (upgrade amount = " + amount + ") " + basecost + " * 1.07 ^ " + (level - 1) +
                    "\n= " + Formula.getLevelUpCost(basecost, level, amount));
            return;
        }
        if (arg.equals("sql")) {
            StringBuilder sql = new StringBuilder();
            for (int i = 1; i < args.length; i++) sql.append(args[i]).append(" ");
            if (sql.toString().endsWith(" ")) sql = new StringBuilder(sql.substring(0, sql.length() - 1));
            String error = null;
            try {
                IdlePokemon.getBot().getDatabase().updateThrowable(sql.toString());
            } catch (SQLException ex) {
                error = ex.getMessage();
            }
            if (error == null)
                e.reply(MessageUtil.success("Dev SQL", "Successfully executed SQL Statement!\nSQL: `" + sql + "`"));
            else e.reply(MessageUtil
                    .err("Dev SQL", "Error executing the SQL statement!\nError: `" + error + "`\nSQL: `" + sql + "`"));
            return;
        }
        if (arg.equals("stats")) {
            OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
            double avgSystemLoad = os.getSystemLoadAverage();
            MemoryMXBean mm = ManagementFactory.getMemoryMXBean();
            MemoryUsage heap = mm.getHeapMemoryUsage();
            long heapUsed = heap.getUsed() / 1024 / 1024;
            long heapMax = heap.getMax() / 1024 / 1024;
            double avg_ping = IdlePokemon.getBot().getShardManager().getAverageGatewayPing();
            int activeThreads = Thread.activeCount();
            long now = System.currentTimeMillis();
            long started = now - IdlePokemon.getStarted();
            int sh = 0;
            int sm = 0;
            int ss = 0;
            while (started >= 1000) {
                started -= 1000;
                ss++;
            }
            while (ss >= 60) {
                ss -= 60;
                sm++;
            }
            while (sm >= 60) {
                sm -= 60;
                sh++;
            }
            long loaded = now - IdlePokemon.getLoaded();
            int lh = 0;
            int lm = 0;
            int ls = 0;
            while (loaded >= 1000) {
                loaded -= 1000;
                ls++;
            }
            while (ls >= 60) {
                ls -= 60;
                lm++;
            }
            while (lm >= 60) {
                lm -= 60;
                lh++;
            }
            String startedString = sh + "h, " + sm + "min and " + ss + " second(s) ago";
            String loadedString = lh + "h, " + lm + "min and " + ls + " second(s) ago";
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Discraft Stats");
            eb.addField("Ping", avg_ping + "ms avg. gateway\n" + Database.ping + "ms latest database request", true);
            eb.addField("Shard Ping",
                    e.getJDA().getGatewayPing() + "ms gateway\n" + e.getJDA().getRestPing().complete() +
                    "ms rest queue", true);
            eb.addField("OS - System Load", avgSystemLoad + " average", true);
            eb.addField("Heap Memory", heapUsed + "M / " + heapMax + "M used", true);
            eb.addField("Threads", activeThreads + " active threads", true);
            eb.addField("SQL Stats", Database.updates + " updates\n" + Database.queries + " queries", true);
            eb.addField("Uptime", "Bot started " + startedString + "\nBot loaded " + loadedString, false);
            e.reply(eb.build());
            return;
        }
        e.reply(MessageUtil.err("Error", "Invalid argument"));
    }
}