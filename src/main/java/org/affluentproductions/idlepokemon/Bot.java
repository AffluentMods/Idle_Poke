package org.affluentproductions.idlepokemon;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.affluentproductions.idlepokemon.db.Database;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bot extends ListenerAdapter {

    private final String token;
    private final int shards;
    private List<Runnable> doneTasks = new ArrayList<>();
    private ShardManager shardManager;
    private Database database;

    public Bot(boolean test) {
        this.database = new Database();
        this.token =
                IdlePokemon.test ? "Njc3NTAyNTIwNzQ2MTgwNjE5.XkVLbQ.KvBhypZc94gQtN4Kua5u2P0bpbw" :
                        "NjcwMjE2Mzg2ODI3NzgwMTA2.Xj8saQ.FpmxTXqVNfwLKvdYZoNm7wA7_QM";
        this.shards = test ? 1 : 5;
    }

    public List<JDA> getShards() {
        return shardManager.getShards();
    }

    public int getTotalGuilds() {
        int totalGuilds = 0;
        for (JDA shard : getShards()) totalGuilds += shard.getGuilds().size();
        return totalGuilds;
    }

    public long getTotalUsers() {
        int totalUsers = 0;
        for (JDA shard : getShards()) totalUsers += shard.getUsers().size();
        return totalUsers;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Database getDatabase() {
        return database;
    }

    public void addDoneTask(Runnable runnable) {
        doneTasks.add(runnable);
    }

    public void start() {
        try {
            if (!database.isConnected()) {
                System.out.println("[FATAL] Database could not connect! Stopping...");
                System.exit(0);
                return;
            }
            this.shardManager = new DefaultShardManagerBuilder(token).setShardsTotal(shards).setAutoReconnect(true)
                    .setActivity(Activity.watching("loading process")).setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .addEventListeners(this).build();
            addDoneTask(IdlePokemon::updateStatus);
            addDoneTask(() -> new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    IdlePokemon.dblapi.setStats(IdlePokemon.getBot().getTotalGuilds());
                }
            }, 60 * 1000, 10 * 60 * 1000));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadBot() {
        for (Runnable r : doneTasks) r.run();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        readyShard(event.getJDA());
    }

    private int loadedShards = 0;

    private void readyShard(JDA jda) {
        if (IdlePokemon.fullyloaded) return;
        loadedShards++;
        if (loadedShards == shards) {
            System.out.println("All shards loaded. Loading bot instance...");
            loadBot();
        }
    }
}