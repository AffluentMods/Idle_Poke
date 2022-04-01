package org.affluentproductions.idlepokemon.entity;

import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.pokemon.sub1.*;
import org.affluentproductions.idlepokemon.entity.pokemon.sub2.*;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.upgrade.Upgrade;
import org.affluentproductions.idlepokemon.upgrade.UpgradeData;
import org.affluentproductions.idlepokemon.util.EmoteUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Pokemon {

    private static final HashMap<Integer, Pokemon> pokemons = new HashMap<>();
    private static final HashMap<Integer, HashMap<Integer, Upgrade>> upgrades = new HashMap<>();

    private final int ID;
    private final String name;
    private BigInteger baseCost;
    private BigDecimal dps;
    private BigDecimal cd;
    private final int level;

    public static Pokemon createPokemon(int ID, String name, BigInteger cost, BigDecimal dps, BigDecimal cd) {
        return new Pokemon(ID, name, cost, dps, cd, 1);
    }

    public static Pokemon getPokemon(int ID) {
        return new HashMap<>(pokemons).get(ID);
    }

    public static Pokemon getPokemon(int ID, int level) {
        Pokemon p = getPokemon(ID);
        return new Pokemon(p.getID(), p.getName(), p.getBaseCost(), p.getDps(), p.getCd(), level);
    }

    public Pokemon(final int ID, final int level) {
        this(ID, getPokemon(ID).getName(), getPokemon(ID).getBaseCost(), getPokemon(ID).getDps(),
                getPokemon(ID).getCd(), level);
    }

    protected Pokemon(final int ID, String name, long baseCost, double dps, double cd) {
        this(ID, name, baseCost, dps, cd, 1);
    }

    protected Pokemon(final int ID, String name, BigInteger baseCost, double dps, double cd) {
        this(ID, name, baseCost, BigDecimal.valueOf(dps), BigDecimal.valueOf(cd), 1);
    }

    protected Pokemon(final int ID, String name, long baseCost, double dps, double cd, int level) {
        this(ID, name, new BigInteger(baseCost + ""), BigDecimal.valueOf(dps), BigDecimal.valueOf(cd), level);
    }

    protected Pokemon(final int ID, String name, BigInteger baseCost, BigDecimal dps, BigDecimal cd, int level) {
        this.ID = ID;
        this.name = name;
        this.baseCost = baseCost;
        this.dps = dps;
        this.cd = cd;
        this.level = level;
    }

    public static void loadPokemons() {
        List<Pokemon> poks = new ArrayList<>(
                Arrays.asList(new Pichu(), new Cubone(), new Jigglypuff(), new Blissey(), new Bulbasaur(),
                        new Squirtle(), new Charmander(), new Pikachu(), new Eevee(), new Shedinja(), new Heracross(),
                        new Politoed(), new Weavile(), new Crobat(), new Gengar(), new Vaporeon(), new Espeon(),
                        new Spiritomb(), new Golurk(), new Gardevoir(), new Volcarona(), new PorygonZ(), new Ludicolo(),
                        new Ninetales(), new Slaking(), new Lapras(), new Mienshao(), new Kingdra(), new Zoroark(),
                        new Snorlax()));
        try (ResultSet rs2 = IdlePokemon.getBot().getDatabase()
                .query("SELECT * FROM server_pokemons WHERE public=1;")) {
            while (rs2.next()) {
                int pid = rs2.getInt("ID");
                String name = rs2.getString("name");
                BigInteger cost = new BigDecimal(rs2.getString("cost")).toBigInteger();
                BigDecimal dps = new BigDecimal(rs2.getString("dps"));
                BigDecimal cd = new BigDecimal(rs2.getString("cd"));
                Pokemon p = Pokemon.createPokemon(pid, name, cost, dps, cd);
                try (ResultSet rs = IdlePokemon.getBot().getDatabase()
                        .query("SELECT * FROM server_upgrades WHERE pid=? ORDER BY ID ASC;", pid)) {
                    while (rs.next()) {
                        Bonus bonus = null;
                        String bonusData = rs.getString("bonus");
                        if (bonusData.contains("|")) {
                            bonus = new Bonus(Double.parseDouble(bonusData.split("|")[0]),
                                    BonusType.valueOf(bonusData.split("|")[1].toUpperCase()));
                        }
                        Upgrade upgrade = new Upgrade(rs.getInt("ID"), rs.getInt("minLevel"),
                                new UpgradeData(bonus, rs.getInt("critchance"), rs.getInt("critmultiplier"),
                                        new BigDecimal(rs.getString("dpsMultiplier")),
                                        new BigDecimal(rs.getString("cdMultiplier")),
                                        new BigDecimal(rs.getString("cdOfDpsMultiplier")),
                                        Skill.getSkill(rs.getString("unlockSkill"))), rs.getInt("affect"),
                                new BigDecimal(rs.getString("cost")), rs.getString("display"));
                        p.addUpgrade(upgrade);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                poks.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Pokemon pok : poks) {
            pokemons.put(pok.getID(), pok);
        }
    }

    public static Pokemon getRandomPokemon() {
        List<Pokemon> poks = new ArrayList<>(pokemons.values());
        return poks.get(new Random().nextInt(poks.size()));
    }

    public static Pokemon getRandomOwnedPokemon(PokemonUser pu) {
        List<Pokemon> poks = new ArrayList<>(pu.getPokemons().values());
        return poks.get(new Random().nextInt(poks.size()));
    }

    public HashMap<Integer, Upgrade> getUpgrades() {
        return upgrades.getOrDefault(this.getID(), new HashMap<>());
    }

    protected void addUpgrades(List<Upgrade> upgrades0) {
        HashMap<Integer, Upgrade> pokemonUpgrades = upgrades.getOrDefault(this.getID(), new HashMap<>());
        for (Upgrade upgrade : upgrades0)
            pokemonUpgrades.put(upgrade.getUpgradeID(), upgrade);
        upgrades.put(this.getID(), pokemonUpgrades);
    }

    protected void addUpgrade(Upgrade... upgrades0) {
        HashMap<Integer, Upgrade> pokemonUpgrades = upgrades.getOrDefault(this.getID(), new HashMap<>());
        for (Upgrade upgrade : upgrades0)
            pokemonUpgrades.put(upgrade.getUpgradeID(), upgrade);
        upgrades.put(this.getID(), pokemonUpgrades);
    }

    public BigInteger getBaseCost() {
        return baseCost;
    }

    public int getLevel() {
        return level;
    }

    public String getEmote() {
        return EmoteUtil.getEmoteMention(name);
    }

    public String getDisplayName() {
        return getEmote() + " " + getName();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCd() {
        return cd;
    }

    public BigDecimal getDps() {
        return dps;
    }

    public int getID() {
        return ID;
    }
}