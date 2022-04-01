package org.affluentproductions.idlepokemon.entity;

import org.affluentproductions.idlepokemon.IdlePokemon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class RivalUser {

    private static final HashMap<String, RivalUser> cache = new HashMap<>();

    private final String userId;
    private int stage;
    private int maxStage;
    private Rival rival;
    private boolean stayStage;

    public RivalUser(String userId) {
        this.userId = userId;
        load();
        cache();
    }

    private void loadCache(RivalUser cached) {
        this.stage = cached.getStage();
        this.maxStage = cached.getMaxStage();
        this.rival = cached.getRival();
        this.stayStage = cached.isStayStage();
        this.rival = cached.getRival();
    }

    public static void clearCache() {
        cache.clear();
    }

    private void load() {
        if (cache.containsKey(userId)) {
            loadCache(cache.get(userId));
            return;
        }
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT nowStage, stage, stay, rival, rivalHealth FROM profiles WHERE userId=?;", userId)) {
            if (rs.next()) {
                this.stage = rs.getInt("nowStage");
                this.maxStage = rs.getInt("stage");
                if (stage == 0) {
                    stage = maxStage;
                    IdlePokemon.getBot().getDatabase()
                            .update("UPDATE profiles SET nowStage=? WHERE userId=?;", stage, userId);
                }
                int rivalLevel = rs.getInt("rival");
                Rival rival = new Rival(rivalLevel, stage, this);
                rival.setHealth(Double.parseDouble(rs.getString("rivalHealth")));
                this.rival = rival;
                this.stayStage = rs.getInt("stay") == 1;
            } else {
                this.stage = 1;
                this.maxStage = 1;
                this.rival = new Rival(1, 1, this);
                this.stayStage = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        cache();
    }

    public boolean isStayStage() {
        return stayStage;
    }

    public int getStage() {
        return stage;
    }

    public int getMaxStage() {
        return maxStage;
    }

    public Rival getRival() {
        return rival;
    }

    public void setAll(Rival rival, int stage, boolean stay) {
        setAll(rival, stage, maxStage, stay);
    }

    public void setAll(Rival rival, int stage, int maxStage, boolean stay) {
        this.stage = stage;
        if (stage > maxStage) maxStage = stage;
        this.stayStage = stay;
        this.rival = rival;
        this.maxStage = maxStage;
        cache();
        IdlePokemon.getBot().getDatabase()
                .update("UPDATE profiles SET rival=?, nowStage=?, stage=?, rivalHealth=?, stay=? WHERE userId=?;",
                        rival.getLevel(), stage, maxStage, String.valueOf(rival.getHealth()), stay ? 1 : 0, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }
}