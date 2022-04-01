package org.affluentproductions.idlepokemon.entity;

import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.event.economy.EconomyEvent;
import org.affluentproductions.idlepokemon.manager.EventManager;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class EcoUser {

    private static final HashMap<String, EcoUser> cache = new HashMap<>();

    private final String userId;
    private BigInteger souls;
    private BigInteger coins;
    private BigInteger rubies;

    public EcoUser(String userId) {
        this.userId = userId;
        load();
        cache();
    }

    private void loadCache(EcoUser cached) {
        this.souls = cached.getSouls();
        this.coins = cached.getCoins();
        this.rubies = cached.getRubies();
    }

    public static void clearCache() {
        cache.clear();
    }

    private void load() {
        if (cache.containsKey(userId)) {
            loadCache(cache.get(userId));
            return;
        }
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM economy WHERE userId=?;", userId)) {
            if (rs.next()) {
                this.coins = new BigInteger(rs.getString("coins"));
                this.rubies = new BigInteger(rs.getString("rubies"));
                this.souls = new BigInteger(rs.getString("souls"));
            } else {
                IdlePokemon.getBot().getDatabase()
                        .update("INSERT INTO economy (userId, coins, rubies, souls) VALUES (?, ?, ?, ?);", userId, "0",
                                "0", "0");
                load();
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        cache();
    }

    private void cache() {
        cache.put(userId, this);
    }

    public BigInteger getSouls() {
        return souls;
    }

    public void setSouls(BigInteger amount) {
        this.souls = amount;
        cache();
        IdlePokemon.getBot().getDatabase()
                .update("UPDATE economy SET souls=? WHERE userId=?;", souls.toString(), userId);
    }

    public void addSouls(BigInteger amount) {
        setSouls(this.souls.add(amount));
    }

    public void removeSouls(BigInteger amount) {
        setSouls(this.souls.subtract(amount));
    }

    public BigInteger getCoins() {
        return coins;
    }

    public void setCoins(BigInteger amount) {
        final BigInteger old = new BigInteger("" + this.coins);
        this.coins = amount;
        cache();
        IdlePokemon.getBot().getDatabase()
                .update("UPDATE economy SET coins=? WHERE userId=?;", coins.toString(), userId);
        EventManager.callEvent(new EconomyEvent(userId, this, old, this.coins));
    }

    public void addCoins(BigInteger amount) {
        setCoins(this.coins.add(amount));
    }

    public void removeCoins(BigInteger amount) {
        setCoins(this.coins.subtract(amount));
    }

    public BigInteger getRubies() {
        return rubies;
    }

    public void setRubies(BigInteger amount) {
        this.rubies = amount;
        cache();
        IdlePokemon.getBot().getDatabase()
                .update("UPDATE economy SET rubies=? WHERE userId=?;", rubies.toString(), userId);
    }

    public void addRubies(BigInteger amount) {
        StatsUtil.setStat(userId, "rubies-gained",
                new BigInteger(StatsUtil.getStat(userId, "rubies-gained", "0")).add(amount).toString());
        setRubies(this.rubies.add(amount));
    }

    public void removeRubies(BigInteger amount) {
        setRubies(this.rubies.subtract(amount));
    }
}