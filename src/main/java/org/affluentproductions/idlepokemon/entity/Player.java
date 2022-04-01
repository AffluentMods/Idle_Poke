package org.affluentproductions.idlepokemon.entity;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.item.Item;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;
import org.affluentproductions.idlepokemon.util.ClickUtil;
import org.affluentproductions.idlepokemon.util.Formula;
import org.affluentproductions.idlepokemon.util.ShinyUtil;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Player {

    private static final HashMap<String, Player> cache = new HashMap<>();

    private List<String> activeSkills = new ArrayList<>();
    private HashMap<Integer, List<Integer>> boughtUpgrades = new HashMap<>();
    private HashMap<String, Bonus> bonuses = new HashMap<>();
    private HashMap<String, Double> dpsMultiplier = new HashMap<>();
    private HashMap<String, Double> shinyDpsMultiplier = new HashMap<>();
    private HashMap<String, Double> soulDpsMultiplier = new HashMap<>();
    private ArrayList<String> badges = new ArrayList<>();
    private boolean initialized = false;
    private final String userId;
    private BigDecimal dps = new BigDecimal("-3");
    private BigDecimal cd = new BigDecimal("-3");
    private int ID;
    private String prefix;
    private int clanID;
    private HashMap<String, Integer> products;

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return IdlePokemon.getBot().getShardManager().getUserById(userId);
    }

    public String getAsTag() {
        return getUser().getAsTag();
    }

    public static boolean isPlayer(String userId) {
        boolean a = false;
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT userId FROM profiles WHERE userId=?;", userId)) {
            if (rs.next()) a = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return a;
    }

    public boolean isActive() {
        boolean a = false;
        long min = Long.parseLong(Constants.minActive);
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT lastActive FROM profiles WHERE userId=?;", userId)) {
            if (rs.next()) {
                String las = rs.getString("lastActive");
                long lastActive;
                if (las.equalsIgnoreCase("")) lastActive = System.currentTimeMillis();
                else {
                    try {
                        lastActive = Long.parseLong(las);
                    } catch (NumberFormatException ex) {
                        lastActive = System.currentTimeMillis();
                    }
                }
                a = lastActive >= min;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return a;
    }

    public void updateActive() {
        IdlePokemon.getBot().getDatabase()
                .update("UPDATE profiles SET lastActive=? WHERE userId=?;", String.valueOf(System.currentTimeMillis()),
                        userId);
    }

    public Player(String userId) {
        this(userId, true);
    }

    public Player(String userId, boolean doLoad) {
        this.userId = userId;
        loadEntity();
        if (doLoad && !isInitialized()) {
            initialized = true;
            loadDefaultPokemons();
        }
        cache();
        if (getPokemonUser().getPokemons().size() < 2) loadDefaultPokemons();
        updateDPS();
        updateCD();
    }

    public PokemonUser getPokemonUser() {
        return new PokemonUser(userId);
    }

    public EcoUser getEcoUser() {
        return new EcoUser(userId);
    }

    public RivalUser getRivalUser() {
        return new RivalUser(userId);
    }

    public boolean isInitialized() {
        return initialized;
    }

    private void loadDefaultPokemons() {
        HashMap<Integer, Pokemon> pokemons = getPokemons();
        List<Pokemon> toAdd = new ArrayList<>(Arrays.asList(Pokemon.getPokemon(1, 1), Pokemon.getPokemon(2, 1)));
        List<Pokemon> remove = new ArrayList<>();
        for (Pokemon pokemon : pokemons.values()) {
            for (Pokemon add : toAdd) {
                if (pokemon.getID() == add.getID()) {
                    remove.add(add);
                }
            }
        }
        for (Pokemon a : toAdd) {
            if (remove.contains(a)) {
                continue;
            }
            setPokemon(a);
        }
        updateDPS();
        updateCD();
    }

    private void loadCache(Player cached) {
        this.ID = cached.getID();
        this.clanID = cached.getClanID();
        this.initialized = cached.isInitialized();
        this.badges = cached.getBadges();
        this.activeSkills = cached.getActiveSkills();
        this.dpsMultiplier = cached.getDpsMultiplier();
        this.shinyDpsMultiplier = cached.getShinyDpsMultiplier();
        this.soulDpsMultiplier = cached.getSoulDpsMultiplier();
        this.bonuses = cached.getBonuses();
        this.products = cached.getProducts();
        this.boughtUpgrades = cached.getBoughtUpgrades();
        this.prefix = cached.getPrefix();
        this.cd = cached.getClickDamage();
        this.dps = cached.getDPS();
    }

    public void setPrefix(String prefix) throws SQLException {
        this.prefix = prefix;
        cache();
        IdlePokemon.getBot().getDatabase().update("DELETE FROM userprefix WHERE userId=?;", userId);
        IdlePokemon.getBot().getDatabase().updateThrowable("INSERT INTO userprefix VALUES (?, ?);", userId, prefix);
    }

    public String getPrefix() {
        return prefix;
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public void setProduct(String productName, int amount) {
        products.remove(productName.toLowerCase());
        if (amount > 0) products.put(productName.toLowerCase(), amount);
        cache();
        IdlePokemon.getBot().getDatabase()
                .update("DELETE FROM userProducts WHERE userId=? AND product=?;", userId, productName.toLowerCase());
        if (amount > 0) IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO userProducts VALUES (?, ?, ?);", userId, productName.toLowerCase(), amount);
    }

    public long getTotalLevels() {
        long totalLevels = 0;
        HashMap<Integer, Pokemon> pokemons = getPokemonUser().getPokemons();
        for (Pokemon pok : pokemons.values()) {
            totalLevels += pok.getLevel();
        }
        return totalLevels;
    }

    public long getObtainableSouls() {
        long totalLevels = getTotalLevels();
        return new Double("" + Math.floor(totalLevels / 2000.0)).longValue();
    }

    public void evolve(boolean reset) {
        ClickUtil.preventClick(userId, 10000);
        BigInteger toAdd = new BigInteger(getObtainableSouls() + "");
        getEcoUser().addSouls(toAdd);
        if (reset) reset(false);
        int evolutions = getEvolutions();
        evolutions++;
        IdlePokemon.getBot().getDatabase().update("DELETE FROM evolutions WHERE userId=?;", userId);
        IdlePokemon.getBot().getDatabase().update("INSERT INTO evolutions VALUES (?, ?)", userId, evolutions);
        StatsUtil.setStat(userId, "souls-obtained",
                new BigInteger(StatsUtil.getStat(userId, "souls-obtained", "0")).add(toAdd).toString());
        ClickUtil.preventClick(userId, -1);
    }

    public void reset(boolean full) {
        List<Integer> pids = new ArrayList<>(getPokemonUser().getPokemons().keySet());
        for (int pid : pids) setPokemon(Pokemon.getPokemon(pid, 0));
        getEcoUser().setCoins(BigInteger.ZERO);
        getRivalUser().setAll(new Rival(1, 1, getRivalUser()), 1, 1, false);
        clearBoughtUpgrades();
        List<String> r1 = new ArrayList<>(bonuses.keySet());
        List<String> r2 = new ArrayList<>(dpsMultiplier.keySet());
        for (String r : r2) {
            if (r.equalsIgnoreCase("Vote DPS Boost")) continue;
            removeDPSMultiplier(r);
        }
        for (String r : r1) {
            if (r.equalsIgnoreCase("Vote Coin Gain")) continue;
            Item item = Item.getItem(r);
            if (item != null) continue;
            removeBonus(r);
        }
        List<String> toReset = new ArrayList<>();
        if (full) {
            toReset.addAll(products.keySet());
        } else {
            toReset.add("clangorous soul");
        }
        for (String r : toReset) setProduct(r, 0);
        if (full) {
            getEcoUser().setRubies(BigInteger.ZERO);
            getEcoUser().setSouls(BigInteger.ZERO);
            List<String> r3 = new ArrayList<>(badges);
            for (String r : r3) removeBonus(r);
        }
    }

    public int getEvolutions() {
        int evolutions = 0;
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM evolutions WHERE userId=?;", userId)) {
            if (rs.next()) evolutions = rs.getInt("evolutions");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return evolutions;
    }

    public int getID() {
        return ID;
    }

    public int getClanID() {
        return clanID;
    }

    public HashMap<String, Bonus> getBonuses() {
        return bonuses;
    }

    public HashMap<Integer, List<Integer>> getBoughtUpgrades() {
        return boughtUpgrades;
    }

    public List<Bonus> getUpgrades(BonusType bt) {
        List<Bonus> a = new ArrayList<>();
        for (int pid : boughtUpgrades.keySet()) {
            List<Integer> uids = boughtUpgrades.get(pid);
            HashMap<Integer, Upgrade> pus = Pokemon.getPokemon(pid).getUpgrades();
            for (int u : uids) {
                if (hasUpgrade(pid, u)) {
                    Upgrade upgrade = pus.get(u);
                    Bonus ub = upgrade.getUpgradeData().getBonus();
                    if (ub.getBonusType() == bt) {
                        a.add(ub);
                        break;
                    }
                }
            }
        }
        return a;
    }

    public void addBonus(String name, Bonus bonus) {
        bonuses.put(name.toLowerCase(), bonus);
        cache();
    }

    public void removeBonus(String name) {
        bonuses.remove(name.toLowerCase());
        cache();
    }

    public HashMap<String, Double> getDpsMultiplier() {
        return dpsMultiplier;
    }

    public HashMap<String, Double> getShinyDpsMultiplier() {
        return shinyDpsMultiplier;
    }

    public HashMap<String, Double> getSoulDpsMultiplier() {
        return soulDpsMultiplier;
    }

    public int getCritChance() {
        int crit = 10; // 10%
        HashMap<Integer, Pokemon> pokemons = getPokemons();
        for (Pokemon pokemon : pokemons.values()) {
            HashMap<Integer, Upgrade> upgrades = pokemon.getUpgrades();
            for (Upgrade upgrade : upgrades.values())
                if (hasUpgrade(pokemon, upgrade))
                    if (upgrade.getUpgradeData().getCritChance() > 0) crit += upgrade.getUpgradeData().getCritChance();
        }
        return crit;
    }

    public double getCritMultiplier() {
        List<Double> cmpUpgrades = new ArrayList<>();
        double cmp = 2.0;
        HashMap<Integer, Pokemon> pokemons = getPokemons();
        for (Pokemon pokemon : pokemons.values()) {
            HashMap<Integer, Upgrade> upgrades = pokemon.getUpgrades();
            for (Upgrade upgrade : upgrades.values()) {
                if (hasUpgrade(pokemon, upgrade)) {
                    if (upgrade.getUpgradeData().getCritmultiplier() > 0) {
                        cmpUpgrades.add(upgrade.getUpgradeData().getCritmultiplier() / 100.0);
                    }
                }
            }
        }
        for (Bonus bonus : getBonuses().values()) {
            if (bonus.getBonusType() == BonusType.CRITICAL_HIT) cmp += cmp * bonus.getValue();
        }
        for (double mp : cmpUpgrades) cmp += cmp * mp;
        return cmp;
    }

    public void addDPSMultiplier(String name, double multiply) {
        dpsMultiplier.put(name.toLowerCase(), multiply);
        cache();
    }

    public void removeDPSMultiplier(String name) {
        dpsMultiplier.remove(name.toLowerCase());
        cache();
    }

    public void addShinyDPSMultiplier(String name, double multiply) {
        shinyDpsMultiplier.put(name.toLowerCase(), multiply);
        cache();
    }

    public void removeShinyDPSMultiplier(String name) {
        shinyDpsMultiplier.remove(name.toLowerCase());
        cache();
    }

    public void addSoulDPSMultiplier(String name, double multiply) {
        soulDpsMultiplier.put(name.toLowerCase(), multiply);
        cache();
    }

    public void removeSoulDPSMultiplier(String name) {
        soulDpsMultiplier.remove(name.toLowerCase());
        cache();
    }

    public void updateDPS() {
        dps = getDPS(true);
    }

    public void updateCD() {
        cd = getClickDamage(true);
    }

    public BigDecimal getClickDamageOfPokemon(Pokemon pokemon) {
        BigDecimal pokCD = pokemon.getCd().multiply(BigDecimal.valueOf(pokemon.getLevel()));
        HashMap<Integer, Upgrade> upgrades = pokemon.getUpgrades();
        for (int uid : upgrades.keySet()) {
            Upgrade upgrade = upgrades.get(uid);
            if (hasUpgrade(pokemon, upgrade)) {
                UpgradeData upgradeData = upgrade.getUpgradeData();
                if (upgradeData.getCdMultiplier().compareTo(BigDecimal.ZERO) > 0) {
                    if (upgrade.getAffectPokemon() != -1)
                        pokCD = pokCD.add(pokCD.multiply(upgradeData.getCdMultiplier()));
                }
            }
        }
        return pokCD;
    }

    public BigDecimal getClickDamage() {
        return getClickDamage(false);
    }

    public BigDecimal getClickDamage(boolean update) {
        if (!update) {
            if (cd.compareTo(new BigDecimal("-3")) == 0) return getClickDamage(true);
            return cd;
        }
        List<BigDecimal> cdOfDpsUpgrades = new ArrayList<>();
        List<BigDecimal> cdupgrades = new ArrayList<>();
        BigDecimal cdamage = new BigDecimal("0");
        HashMap<Integer, Pokemon> pokemons = getPokemons();
        for (Pokemon pokemon : pokemons.values()) {
            BigDecimal pokCD = getClickDamageOfPokemon(pokemon);
            HashMap<Integer, Upgrade> upgrades = pokemon.getUpgrades();
            for (int uid : upgrades.keySet()) {
                Upgrade upgrade = upgrades.get(uid);
                if (hasUpgrade(pokemon, upgrade)) {
                    UpgradeData upgradeData = upgrade.getUpgradeData();
                    if (upgradeData.getCdMultiplier().compareTo(BigDecimal.ZERO) > 0) {
                        if (upgrade.getAffectPokemon() == -1) cdupgrades.add(upgradeData.getCdMultiplier());
                    }
                    if (upgradeData.getCdOfDpsMultiplier().compareTo(BigDecimal.ZERO) > 0) {
                        cdOfDpsUpgrades.add(upgradeData.getCdOfDpsMultiplier());
                    }
                }
            }
            cdamage = cdamage.add(pokCD);
        }
        for (String bonus : bonuses.keySet()) {
            Bonus theBonus = bonuses.get(bonus);
            BonusType bt = theBonus.getBonusType();
            if (bt == BonusType.CLICK_DAMAGE || bt == BonusType.FULL_DAMAGE)
                cdamage = cdamage.add(cdamage.multiply(BigDecimal.valueOf(theBonus.getValue())));
        }
        for (BigDecimal mp : cdupgrades) cdamage = cdamage.add(cdamage.multiply(mp));
        BigDecimal dps = getDPS(false);
        for (BigDecimal mp : cdOfDpsUpgrades) cdamage = cdamage.add(dps.multiply(mp));
        cd = cdamage;
        cache();
        return cdamage;
    }

    public BigDecimal getDPSofPokemon(Pokemon pokemon) {
        BigDecimal pokemonsDPS = Formula.getDPSperGold(pokemon.getLevel(), pokemon.getDps());
        BigDecimal multiplicand = BigDecimal.valueOf(ShinyUtil.getShinies(userId, pokemon) / 10.0);
        pokemonsDPS = pokemonsDPS.add(pokemonsDPS.multiply(multiplicand));
        HashMap<Integer, Upgrade> upgrades = pokemon.getUpgrades();
        for (int uid : upgrades.keySet()) {
            Upgrade upgrade = upgrades.get(uid);
            if (hasUpgrade(pokemon, upgrade)) {
                UpgradeData upgradeData = upgrade.getUpgradeData();
                if (upgradeData.getDpsMultiplier().compareTo(BigDecimal.ZERO) > 0) {
                    if (upgrade.getAffectPokemon() != -1) {
                        pokemonsDPS = pokemonsDPS.add(upgradeData.getDpsMultiplier().multiply(pokemonsDPS));
                    }
                }
            }
        }
        return pokemonsDPS;
    }

    public BigDecimal getDPS() {
        return getDPS(false);
    }

    public BigDecimal getDPS(boolean update) {
        if (!update) {
            if (dps.compareTo(new BigDecimal("-3")) == 0) return getDPS(true);
            return dps;
        }
        List<BigDecimal> dpsupgrades = new ArrayList<>();
        BigDecimal dps = new BigDecimal("0");
        HashMap<Integer, Pokemon> pokemons = getPokemons();
        for (Pokemon pokemon : pokemons.values()) {
            BigDecimal pokemonsDPS = getDPSofPokemon(pokemon);
            HashMap<Integer, Upgrade> upgrades = pokemon.getUpgrades();
            for (int uid : upgrades.keySet()) {
                Upgrade upgrade = upgrades.get(uid);
                if (hasUpgrade(pokemon, upgrade)) {
                    UpgradeData upgradeData = upgrade.getUpgradeData();
                    if (upgradeData.getDpsMultiplier().compareTo(BigDecimal.ZERO) > 0) {
                        if (upgrade.getAffectPokemon() == -1) {
                            dpsupgrades.add(upgradeData.getDpsMultiplier());
                        }
                    }
                }
            }
            dps = dps.add(pokemonsDPS);
        }
        HashMap<String, Double> dpsmp = getDpsMultiplier();
        for (double mp : dpsmp.values()) dps = dps.add(dps.multiply(BigDecimal.valueOf(mp)));
        for (BigDecimal mp : dpsupgrades) dps = dps.add(dps.multiply(mp));
        BigDecimal soulsMP = new BigDecimal(getEcoUser().getSouls()).multiply(BigDecimal.valueOf(0.1));
        HashMap<String, Double> souldpsmp = getSoulDpsMultiplier();
        for (double mp : souldpsmp.values()) soulsMP = soulsMP.add(BigDecimal.valueOf(mp));
        dps = dps.add(dps.multiply(soulsMP));
        this.dps = dps;
        cache();
        return dps;
    }

    public boolean hasUpgrade(Pokemon pokemon, Upgrade upgrade) {
        return hasUpgrade(pokemon.getID(), upgrade.getUpgradeID());
    }

    public boolean hasUpgrade(int pid, int upgradeID) {
        return boughtUpgrades.getOrDefault(pid, new ArrayList<>()).contains(upgradeID);
    }

    public void addBoughtUpgrade(Pokemon pokemon, Upgrade upgrade) {
        List<Integer> bought = boughtUpgrades.getOrDefault(pokemon.getID(), new ArrayList<>());
        if (!bought.contains(upgrade.getUpgradeID())) bought.add(upgrade.getUpgradeID());
        boughtUpgrades.put(pokemon.getID(), bought);
        cache();
        IdlePokemon.getBot().getDatabase()
                .update("INSERT INTO upgrades VALUES (?, ?, ?);", userId, pokemon.getID(), upgrade.getUpgradeID());
    }

    public void clearBoughtUpgrades() {
        boughtUpgrades.clear();
        cache();
        IdlePokemon.getBot().getDatabase().update("DELETE FROM upgrades WHERE userId=?;", userId);
    }

    public List<String> getActiveSkills() {
        return activeSkills;
    }

    public void addActiveSkill(int id) {
        if (!activeSkills.contains(String.valueOf(id))) activeSkills.add(String.valueOf(id));
    }

    public void removeActiveSkill(int id) {
        activeSkills.remove(String.valueOf(id));
    }

    public Pokemon getPokemonByName(String name) {
        return getPokemonUser().getPokemonByName(name);
    }

    public HashMap<Integer, Pokemon> getPokemons() {
        return getPokemonUser().getPokemons();
    }

    public Pokemon getPokemon(int ID) {
        return getPokemonUser().getPokemon(ID);
    }

    public void setPokemon(Pokemon newPokemon) {
        getPokemonUser().setPokemon(newPokemon);
    }

    public ArrayList<String> getBadges() {
        return badges;
    }

    public void addBadge(String badgeName) {
        badges.add(badgeName.toLowerCase());
        cache();
        IdlePokemon.getBot().getDatabase().update("INSERT INTO badges VALUES (?, ?);", userId, badgeName.toLowerCase());
    }

    public void removeBadge(String badgeName) {
        badges.remove(badgeName.toLowerCase());
        cache();
        IdlePokemon.getBot().getDatabase()
                .update("DELETE FROM badges WHERE userId=? AND badgeName=?;", userId, badgeName.toLowerCase());
    }

    private void loadEntity() {
        if (cache.containsKey(userId)) {
            loadCache(cache.get(userId));
            return;
        }
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM profiles WHERE userId=?;", userId)) {
            if (rs.next()) {
                ID = rs.getInt("ID");
                clanID = rs.getInt("clan");
            } else {
                String now = "" + System.currentTimeMillis();
                IdlePokemon.getBot().getDatabase()
                        .update("INSERT INTO profiles (userId, clan, nowStage, stage, rival, rivalHealth, lastActive)" +
                                " VALUES " + "(?, ?, ?, ?, ?, ?, ?);", userId, -1, 1, 1, 1,
                                Formula.getRivalHealth(1, false), now);
                loadEntity();
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM badges WHERE userId=?;", userId)) {
            while (rs.next()) {
                String badge = rs.getString("badgeName");
                badges.add(badge);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM upgrades WHERE userId=?;", userId)) {
            while (rs.next()) {
                int pokemonID = rs.getInt("pokemonID");
                int upgradeID = rs.getInt("upgradeID");
                List<Integer> bought = boughtUpgrades.getOrDefault(pokemonID, new ArrayList<>());
                bought.add(upgradeID);
                boughtUpgrades.put(pokemonID, bought);
                try {
                    Pokemon pokemon = Pokemon.getPokemon(pokemonID);
                    Upgrade upgrade = pokemon.getUpgrades().getOrDefault(upgradeID, null);
                    if (upgrade.getUpgradeData().getBonus().getBonusType() != BonusType.NULL)
                        addBonus(pokemon.getName() + " U" + upgradeID, upgrade.getUpgradeData().getBonus());
                } catch (NullPointerException ignored) {
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        products = new HashMap<>();
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM userProducts WHERE userId=?;", userId)) {
            while (rs.next()) {
                String product = rs.getString("product");
                int amount = rs.getInt("amount");
                products.put(product.toLowerCase(), amount);
                if (Skill.getSkill(product) != null) {
                    Skill skill = Skill.getSkill(product);
                    if (skill.getEffect().isProduct()) {
                        skill.getEffect().reactivate(this, amount);
                    }
                } else {
                    Item item = Item.getItem(product);
                    if (item.getBonus().getBonusType() != BonusType.NULL) addBonus(product, item.getBonus());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM userprefix WHERE userId=?;", userId)) {
            if (rs.next()) {
                prefix = rs.getString("prefix");
            } else {
                prefix = IdlePokemon.test ? Constants.TEST_PREFIX : Constants.PREFIX;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        cache();
        updateDPS();
        updateCD();
    }

    public static void init() {
        IdlePokemon.getBot().getDatabase().ct("userprefix", "userId VARCHAR(24) NOT NULL, prefix VARCHAR(4) NOT NULL");
        IdlePokemon.getBot().getDatabase().ct("evolutions", "userId VARCHAR(24) NOT NULL, evolutions INT NOT NULL");
        IdlePokemon.getBot().getDatabase()
                .ct("upgrades", "userId VARCHAR(24) NOT NULL, pokemonID INT NOT NULL, upgradeID INT NOT NULL");
        IdlePokemon.getBot().getDatabase().ct("badges", "userId VARCHAR(24) NOT NULL, badgeName VARCHAR(64) NOT NULL");
        IdlePokemon.getBot().getDatabase().ct("pokemons",
                "ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, userId VARCHAR(24) NOT NULL, pid INT NOT NULL, " +
                "level INT NOT NULL");
        IdlePokemon.getBot().getDatabase().ct("economy",
                "userId VARCHAR(24) NOT NULL, coins VARCHAR(128) NOT NULL, rubies VARCHAR(64) NOT NULL, souls VARCHAR" +
                "(64) NOT NULL");
        IdlePokemon.getBot().getDatabase().ct("profiles",
                "ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, userId VARCHAR(24) NOT NULL, nowStage INT NOT NULL, " +
                "stage INT NOT NULL, stay INT(2) NOT NULL, rival INT NOT NULL, rivalHealth VARCHAR(32) NOT NULL, clan" +
                " INT NOT NULL, lastActive VARCHAR(24) NOT NULL");
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static void clearCache() {
        cache.clear();
    }
}