package org.affluentproductions.idlepokemon.ancient;

import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.entity.Player;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Ancients {

    private static final HashMap<String, HashMap<Integer, Integer>> cache = new HashMap<>();
    private static final HashMap<Integer, Ancient> ancients = new HashMap<>();

    public static void load() {
        List<Ancient> ancientList =
                Arrays.asList(new GiratinaAncient(), new PalkiaAncient(), new RegisteelAncient(), new GroudonAncient(),
                        new RegirockAncient(), new RayquazaAncient(), new ArceusAncient(), new HeatranAncient(),
                        new GenesectAncient(), new CarbinkAncient(), new DreepyAncient());
        for (Ancient ancient : ancientList) {
            ancients.put(ancient.getID(), ancient);
        }
        IdlePokemon.getBot().getDatabase()
                .ct("ancients", "userId VARCHAR(24) NOT NULL, ancientID INT NOT NULL, level INT NOT NULL");
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM ancients;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                int ancientID = rs.getInt("ancientID");
                int level = rs.getInt("level");
                Ancient ancient = Ancients.getAncient(ancientID);
                ancient.reactivate(new Player(userId), level);
                HashMap<Integer, Integer> ua = cache.getOrDefault(userId, new HashMap<>());
                ua.put(ancientID, level);
                cache.put(userId, ua);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void setAncient(String userId, int ancientID, int level) {
        setAncient(userId, Ancients.getAncient(ancientID), level);
    }

    public static void setAncient(String userId, Ancient ancient, int level) {
        int ancientID = ancient.getID();
        ancient.summon(new Player(userId), level);
        HashMap<Integer, Integer> ua = cache.getOrDefault(userId, new HashMap<>());
        ua.put(ancientID, level);
        cache.put(userId, ua);
        IdlePokemon.getBot().getDatabase()
                .update("DELETE FROM ancients WHERE userId=? AND ancientID=?;", userId, ancientID);
        IdlePokemon.getBot().getDatabase().update("INSERT INTO ancients VALUES (? ,?, ?);", userId, ancientID, level);
    }

    public static Ancient getAncient(int ID) {
        return ancients.get(ID);
    }

    public static boolean hasAncient(String userId, Ancient ancient) {
        return getAncientLv(userId, ancient) != -1;
    }

    public static int getAncientLv(String userId, Ancient ancient) {
        HashMap<Integer, Integer> ua = cache.getOrDefault(userId, new HashMap<>());
        if (ua.containsKey(ancient.getID())) return ua.get(ancient.getID());
        int lv = -1;
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM ancients WHERE userId=? AND ancientID=?;", userId, ancient.getID())) {
            if (rs.next()) lv = rs.getInt("level");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ua.put(ancient.getID(), lv);
        cache.put(userId, ua);
        return lv;
    }

    public static int getOwnedAncients(String uid) {
        int oa = 0;
        TreeMap<Integer, Ancient> ancients = new TreeMap<>(Ancients.getAncients());
        for (Ancient ancient : ancients.values()) {
            if (Ancients.hasAncient(uid, ancient)) oa++;
        }
        return oa;
    }

    public static BigInteger getSummonCost(int ownedAncients) {
        List<String> costs =
                Arrays.asList("1:1", "2:2", "3:4", "4:8", "5:18", "6:40", "7:80", "8:140", "9:275", "10:560", "11:875",
                        "12:1300", "13:1825", "14:2350", "15:2925", "16:3600", "17:4350", "18:5300", "19:6350",
                        "20:7900", "21:10500", "22:13350", "23:18000", "24:27500");
        BigInteger a = BigInteger.ONE;
        for (String cost : costs) {
            int o = Integer.parseInt(cost.split(":")[0]);
            int c = Integer.parseInt(cost.split(":")[1]);
            if (o == (ownedAncients + 1)) {
                a = BigInteger.valueOf(c);
                break;
            }
        }
        return a;
    }

    public static HashMap<Integer, Ancient> getAncients() {
        return ancients;
    }
}