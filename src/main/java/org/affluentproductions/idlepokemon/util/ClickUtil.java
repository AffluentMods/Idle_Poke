package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.click.Click;
import org.affluentproductions.idlepokemon.commands.action.ClickCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.RivalUser;
import org.affluentproductions.idlepokemon.event.action.ClickEvent;
import org.affluentproductions.idlepokemon.manager.EventManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClickUtil {

    public static String dpsdebug = null;

    public static boolean pause = false;
    private static ExecutorService clickerService = Executors.newWorkStealingPool();
    private static ScheduledExecutorService timer = null;
    private static ScheduledExecutorService updateTimer = null;

    public static HashMap<String, Integer> autoClickers = new HashMap<>();
    public static List<String> clickers = new ArrayList<>();
    private static HashMap<String, Long> preventClick = new HashMap<>();

    public static boolean isPreventClick(String userId) {
        long now = System.currentTimeMillis();
        return preventClick.getOrDefault(userId, -1L) >= now;
    }

    public static void preventClick(String userId, long ms) {
        preventClick.remove(userId);
        if (ms > 0) {
            long now = System.currentTimeMillis();
            preventClick.put(userId, now + ms);
        }
    }

    public static void stopClicker() {
        timer.shutdown();
        updateTimer.shutdown();
    }

    public static boolean devlog = false;

    private static long lastClickStart = -1;
    public static long clickStart = -1;
    public static long clickEnd = -1;
    public static long clickPing = -1;

    private static int runs = 0;

    public static void loadClicker() {
        if (timer == null) timer = Executors.newSingleThreadScheduledExecutor();
        if (updateTimer == null) updateTimer = Executors.newSingleThreadScheduledExecutor();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (pause) return;
                try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                        .query("SELECT * FROM profiles WHERE CAST(CONV(profiles.lastActive,16,10) AS UNSIGNED " +
                               "INTEGER) > " + Constants.minActive + ";")) {
                    while (rs.next()) {
                        String userId = rs.getString("userId");
                        if (!clickers.contains(userId)) clickers.add(userId);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                        .query("SELECT * FROM userProducts WHERE product='auto clicker';")) {
                    while (rs.next()) {
                        String userId = rs.getString("userId");
                        int amount = rs.getInt("amount");
                        autoClickers.put(userId, amount);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (dpsdebug != null) IdlePokemon.getBot().getShardManager().getTextChannelById(dpsdebug).sendMessage(
                        "[" + new SimpleDateFormat("HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()) +
                        "] Updated DPS userlist").queue();
            }
        }, 0, 5, TimeUnit.MINUTES);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (pause) return;
                clickStart = System.currentTimeMillis();
                if (lastClickStart > 0) clickPing = clickStart - lastClickStart;
                lastClickStart = clickStart;
                runs++;
                if (dpsdebug != null) IdlePokemon.getBot().getShardManager().getTextChannelById(dpsdebug).sendMessage(
                        "[" + new SimpleDateFormat("HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()) +
                        "] Running DPS Timer (Run #" + runs + " | Click Ping " + clickPing + "ms | " + clickers.size() +
                        " dps users | " + autoClickers.size() + " autoclicker users)").queue();
                for (String userId : autoClickers.keySet()) {
                    if (!isPreventClick(userId)) {
                        clickerService.submit(() -> doAutoClick(userId, autoClickers.get(userId) * 3));
                    }
                }
                for (String userId : clickers) {
                    if (!isPreventClick(userId)) {
                        clickerService.submit(() -> doFullClick(userId));
                    }
                }
                clickEnd = System.currentTimeMillis();
            }
        }, dpsperiod, dpsperiod, TimeUnit.SECONDS);
    }

    private static final int dpsperiod = 5;

    private static void doFullClick(String userId) {
        Player p = new Player(userId);
        doFullClick(p, p.getDPS().multiply(BigDecimal.valueOf(dpsperiod)), 100);
    }

    private static void doAutoClick(String userId, int acs) {
        Player p = new Player(userId);
        doFullClick(p, p.getClickDamage().multiply(BigDecimal.valueOf(dpsperiod)).multiply(BigDecimal.valueOf(acs)),
                100);
        EventManager.callEvent(new ClickEvent(p, null));
    }

    public static void doFullClick(Player p, BigDecimal damage, int maxRounds) {
        HashMap<String, Bonus> bonuses = p.getBonuses();
        RivalUser pr = p.getRivalUser();
        Click click = ClickCommand.doClick(p, pr, pr.getRival(), bonuses, damage, maxRounds);
        pr.setAll(click.getNewRival(), pr.getStage(), pr.isStayStage());
    }
}