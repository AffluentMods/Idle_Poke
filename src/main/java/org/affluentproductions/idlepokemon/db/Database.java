package org.affluentproductions.idlepokemon.db;

import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.util.ClickUtil;

import java.sql.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Database {

    public static int queries = 0;
    public static int updates = 0;
    public static long ping = -1L;
    private Connection con;

    public Database() {
        try {
            final String ip = "91.200.103.0";
            final short port = 3306;
            final String db = IdlePokemon.test ? "idlepokemon_test" : "idlepokemon";
            final String u = "idlepokemon";
            final String p = "IPfOIhw1V5gP8SjR";
            Properties props = new Properties();
            props.put("user", u);
            props.put("password", p);
            props.put("autoReconnect", "true");
            con = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db, props);
            System.out.println("DB intialized to " + ip + "/" + db);
            setup();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void ct(String tn, String ts) {
        update("CREATE TABLE IF NOT EXISTS " + tn + " (" + ts + ");");
    }

    public void setup() {
        ct("server_pokemons",
                "ID INT NOT NULL, name VARCHAR(32) NOT NULL, cost VARCHAR(64) NOT NULL, dps VARCHAR(64) NOT NULL, cd " +
                "VARCHAR(64) NOT NULL, public INT(2) NOT NULL");
        ct("server_upgrades",
                "ID INT NOT NULL, pid INT NOT NULL, minLevel INT NOT NULL, cost VARCHAR(64) NOT NULL, bonus VARCHAR" +
                "(64) NOT NULL, critchance INT" +
                " NOT NULL, critmultiplier INT NOT NULL, dpsMultiplier VARCHAR(64) NOT NULL, cdMultiplier VARCHAR(64)" +
                " NOT NULL, cdOfDpsMultiplier VARCHAR(64) NOT NULL, unlockSkill VARCHAR(64) NOT NULL, affect INT NOT " +
                "NULL, display VARCHAR(128) NOT NULL");
        ct("stats",
                "userId VARCHAR(24) NOT NULL, statistic_name VARCHAR(64) NOT NULL, statistic_value VARCHAR(128) NOT " +
                "NULL");
        ct("skills", "userId VARCHAR(64) NOT NULL, skill INT NOT NULL");
        ct("userProducts", "userId VARCHAR(64) NOT NULL, product VARCHAR(64) NOT NULL, amount INT NOT NULL");
        ct("guides", "guideName VARCHAR(32) NOT NULL, guideText VARCHAR(2000) NOT NULL");
        ct("badges", "userId VARCHAR(24) NOT NULL, badgeName VARCHAR(64) NOT NULL");
        ct("donatorRoleRewards", "bundle INT NOT NULL, rw_item VARCHAR(64) NOT NULL, rw_amount VARCHAR(64) NOT NULL");
        ct("offServerUpdates", "guildId VARCHAR(64) NOT NULL, channelId VARCHAR(64) NOT NULL");
        ct("updatePosts", "updatePost VARCHAR(4096) NOT NULL");
        ct("achievements",
                "userId VARCHAR(24) NOT NULL, achievementName VARCHAR(64) NOT NULL, achievementTier INT NOT NULL");
        ct("cooldowns",
                "userId VARCHAR(64) NOT NULL, cooldownName VARCHAR(64) NOT NULL, cooldownEnd VARCHAR(48) NOT NULL");
        ct("bans", "userId VARCHAR(64) NOT NULL, todate VARCHAR(64) NOT NULL");
        ct("votes", "userId VARCHAR(64) NOT NULL, until bigint(24) NOT NULL");
        ct("lang_messages",
                "ID INT PRIMARY KEY AUTO_INCREMENT, msgID VARCHAR(32) NOT NULL, langCode VARCHAR(6) NOT NULL, message" +
                " VARCHAR(2048) NOT NULL");
        ct("blacklist", "guildId VARCHAR(24) NOT NULL");
    }

    public void disconnect() {
        try {
            con.close();
            con = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet query(String sql, Object... preparedParameters) {
        long now = System.currentTimeMillis();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            int id = 1;
            for (Object preparedParameter : preparedParameters) {
                ps.setObject(id, preparedParameter);
                id++;
            }
            updatePing(now);
            queries++;
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet query(String sql) {
        long now = System.currentTimeMillis();
        try {
            ResultSet rs = con.prepareStatement(sql).executeQuery();
            updatePing(now);
            queries++;
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getCon() {
        return con;
    }

    /*
    Updates the database request ping
     */
    private void updatePing(long start) {
        ping = System.currentTimeMillis() - start;
        if (!ClickUtil.pause && ping > 10000) {
            System.out.println("Database ping updated to " + ping + "ms | Pausing DPS clicker");
            ClickUtil.pause = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Resuming DPS clicker");
                    ClickUtil.pause = false;
                }
            }, 5 * 1000);
        }
    }

    public void updateThrowable(String sql, Object... preparedParameters) throws SQLException {
        long now = System.currentTimeMillis();
        PreparedStatement ps = con.prepareStatement(sql);
        int id = 1;
        for (Object preparedParameter : preparedParameters) {
            ps.setObject(id, preparedParameter);
            id++;
        }
        updates++;
        ps.executeUpdate();
        updatePing(now);
        ps.close();
    }

    public void update(String sql, Object... preparedParameters) {
        long now = System.currentTimeMillis();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int id = 1;
            for (Object preparedParameter : preparedParameters) {
                ps.setObject(id, preparedParameter);
                id++;
            }
            updates++;
            ps.executeUpdate();
            updatePing(now);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String sql) {
        long now = System.currentTimeMillis();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            updates++;
            ps.executeUpdate();
            updatePing(now);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return con != null && !(con.isClosed());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}