package org.affluentproductions.idlepokemon.entity;

import org.affluentproductions.idlepokemon.util.Formula;

public class Rival {

    private int level;
    private String name;
    private int stage;
    private double maxhealth;
    private double health;
    private long spawned;
    private RivalUser ru;

    public Rival(int level, int stage, RivalUser rivalUser) {
        this.ru = rivalUser;
        this.level = level;
        this.stage = stage;
        this.name = Pokemon.getRandomPokemon().getName();
        double health = Formula.getRivalHealth(stage, isBoss());
        this.maxhealth = health;
        this.health = health;
        spawned = System.currentTimeMillis();
    }

    public boolean isBoss() {
        return stage % 10 == 0;
    }

    public String getName() {
        return name;
    }

    public int getStage() {
        return stage;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        int maxLevel = 10;
        if (isBoss()) maxLevel = 1;
        return maxLevel;
    }

    public double getHealth() {
        long now = System.currentTimeMillis();
        if (isBoss()) {
            if (now - spawned >= 30000) {
                if (!ru.isStayStage()) stage--;
                this.level = 1;
                health = Formula.getRivalHealth(stage, isBoss());
                maxhealth = health;
                spawned = now;
            }
        }
        return health;
    }

    public long getSpawned() {
        return spawned;
    }

    public double getMaxhealth() {
        return maxhealth;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}