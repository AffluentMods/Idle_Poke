package org.affluentproductions.idlepokemon;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.affluentproductions.idlepokemon.botlistener.CommandListener;
import org.affluentproductions.idlepokemon.util.ClickUtil;
import org.affluentproductions.idlepokemon.util.LoadUtil;
import org.affluentproductions.idlepokemon.vote.VoteServer;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.*;

public class IdlePokemon {

    public static final List<String> disabledModules = new ArrayList<>();
    public static boolean maintenance = false;
    public static String maintenance_message = "Idle Pokémon is currently in maintenance!";
    public static boolean shuttingdown = false;
    public static boolean fullyloaded = false;
    public static boolean test = false;
    private static Bot bot;
    public static DiscordBotListAPI dblapi = null;
    private static long started;
    private static long loaded;
    public static long restartAt;

    public static long getLoaded() {
        return loaded;
    }

    public static long getStarted() {
        return started;
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("devtest")) {
                test = true;
                break;
            }
        }
        started = System.currentTimeMillis();
        restartAt = started + (18 * 60 * 60 * 1000);
        start(test);
    }

    public static void disable(String module) {
        if (!disabledModules.contains(module.toLowerCase())) disabledModules.add(module.toLowerCase());
    }

    public static void enable(String module) {
        disabledModules.remove(module.toLowerCase());
    }

    private static void start(boolean test) {
        bot = new Bot(test);
        bot.addDoneTask(() -> {
            LoadUtil.load();
            bot.getShardManager().addEventListener(new CommandListener());
            loaded = System.currentTimeMillis();
            dblapi = new DiscordBotListAPI.Builder().botId("670216386827780106").token(Constants.dbl_token).build();
            fullyloaded = true;
        });
        bot.addDoneTask(() -> new Timer().schedule(new TimerTask() {
            int lastShardRestarted = -1;

            @Override
            public void run() {
                if (shuttingdown) {
                    this.cancel();
                    return;
                }
                if (System.currentTimeMillis() >= restartAt) {
                    System.out.println("Auto Restarting");
                    shutdown();
                    return;
                }
                lastShardRestarted++;
                if (lastShardRestarted >= bot.getShardManager().getShardsTotal()) lastShardRestarted = 0;
                bot.getShardManager().restart(lastShardRestarted);
                System.out.println("Restarting shard #" + lastShardRestarted);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            IdlePokemon.updateStatus(
                                    Objects.requireNonNull(bot.getShardManager().getShardById(lastShardRestarted))
                                            .awaitReady());
                        } catch (InterruptedException e) {
                            System.out.println("Error restarting shard " + lastShardRestarted + ": " + e.getMessage());
                        }
                    }
                }, 15000);
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000));
        bot.start();
        Thread ccht = new Thread(() -> {
            System.out.println("Started console command handler");
            final Scanner s = new Scanner(System.in);
            while (s.hasNextLine()) {
                String command = s.nextLine();
                if (command.equalsIgnoreCase("stop")) {
                    shutdown();
                }
            }
        });
        ccht.setName("Console Command Handler Thread");
        ccht.setDaemon(true);
        ccht.start();
    }

    public static void shutdown() {
        shuttingdown = true;
        System.out.println("Stopping clickers...");
        ClickUtil.stopClicker();
        System.out.println("Stopping vote server...");
        VoteServer.stop();
        System.out.println("Shutting down shardmanager..");
        bot.getShardManager().shutdown();
        Thread dbdt = new Thread(() -> {
            System.out.println("Disconnecting database...");
            bot.getDatabase().disconnect();
        });
        dbdt.start();
        System.out.println("Done - exiting");
        System.exit(0);
    }

    public static Bot getBot() {
        return bot;
    }

    public static TextChannel getBotLog() {
        return getBot().getShardManager().getTextChannelById("670068157771284491");
    }

    public static TextChannel getModLog() {
        return getBot().getShardManager().getTextChannelById("669694039120936999");
    }

    public static Guild getHub() {
        return getBot().getShardManager().getGuildById(Constants.main_guild);
    }

    private static void updateStatus(JDA jda) {
        if (fullyloaded) {
            int tg = bot.getTotalGuilds();
            long tu = bot.getTotalUsers();
            jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching(
                    tg + " guilds and " + tu + " users | `" + Constants.PREFIX + "help` [#" +
                    jda.getShardInfo().getShardId() + "]"));
        }
    }

    public static void updateStatus() {
        for (JDA jda : getBot().getShards()) {
            updateStatus(jda);
        }
    }

}