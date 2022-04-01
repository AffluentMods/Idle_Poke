package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.entity.Player;

import java.util.*;

public class Skill {

    private static final HashMap<String, Skill> skills = new HashMap<>();

    private final int ID;
    private final String name;
    private String description;
    private final SkillEffect effect;
    private final int unlockPok;
    private final int unlockUpgrade;
    private final int unlockLv;
    private final long cooldown;

    private Skill(String name) {
        this.ID = -1;
        this.name = name;
        this.effect = new SkillEffect(-1) {
            @Override
            public void activate(Player player, boolean isBuy, boolean doubleEffect) {
            }

            @Override
            public void deactivate(Player player) {
            }
        };
        this.description = "";
        this.unlockPok = -1;
        this.unlockUpgrade = -1;
        this.unlockLv = -1;
        this.cooldown = -1;
    }

    public Skill(int ID, String name, String description, SkillEffect effect, int unlockPokemonID, int unlockUpgradeID,
                 int unlockPokemonLevel, long cooldown) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.unlockPok = unlockPokemonID;
        this.unlockUpgrade = unlockUpgradeID;
        this.unlockLv = unlockPokemonLevel;
        this.cooldown = cooldown;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getUnlockLv() {
        return unlockLv;
    }

    public long getCooldown() {
        return cooldown;
    }

    public int getUnlockUpgrade() {
        return unlockUpgrade;
    }

    public int getUnlockPok() {
        return unlockPok;
    }

    public SkillEffect getEffect() {
        return effect;
    }

    //

    public static void load() {
        List<Skill> skills1 =
                Arrays.asList(new ExtremeSpeed(), new SwordDance(), new FocusEnergy(), new HappyHour(), new PayDay(),
                        new ClangorousSoul(), new CloseCombat(), new HelpingHand(), new Replenish());
        for (Skill skill : skills1) {
            skills.put(skill.getName().toLowerCase(), skill);
        }
    }

    public static TreeMap<Integer, Skill> getSkills() {
        HashMap<Integer, Skill> skills1 = new HashMap<>();
        for (Skill skill : skills.values()) {
            skills1.put(skill.getID(), skill);
        }
        return new TreeMap<>(skills1);
    }

    public static Skill getSkill(int sid) {
        Map<Integer, Skill> skills = getSkills();
        return skills.get(sid);
    }

    public static Skill getSkill(String name) {
        if (!skills.containsKey(name)) return new Skill(name);
        return skills.get(name.toLowerCase());
    }
}