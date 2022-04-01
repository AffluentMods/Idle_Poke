package org.affluentproductions.idlepokemon.upgrade;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.skill.Skill;

import java.math.BigDecimal;

public class UpgradeData {

    private final Bonus bonus;
    private final int critchance;
    private final int critmultiplier;
    private final BigDecimal dpsMultiplier;
    private final BigDecimal cdMultiplier;
    private final BigDecimal cdOfDpsMultiplier;
    private final Skill unlockSkill;

    public UpgradeData(Bonus bonus) {
        this(bonus, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null);
    }

    public UpgradeData(String critchance, String critmultiplier) {
        this(null, Integer.parseInt(critchance), Integer.parseInt(critmultiplier), BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, null);
    }

    public UpgradeData(String critchance) {
        this(null, Integer.parseInt(critchance), 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null);
    }

    public UpgradeData(double dpsMultiplier, double cdMultiplier) {
        this(null, 0, 0, BigDecimal.valueOf(dpsMultiplier), BigDecimal.valueOf(cdMultiplier), BigDecimal.ZERO, null);
    }

    public UpgradeData(Skill unlockSkill) {
        this(null, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, unlockSkill);
    }


    public UpgradeData(double dpsMultiplier, double cdMultiplier, double cdOfDpsMultiplier) {
        this(null, 0, 0, BigDecimal.valueOf(dpsMultiplier), BigDecimal.valueOf(cdMultiplier),
                BigDecimal.valueOf(cdOfDpsMultiplier), null);
    }

    public UpgradeData(Bonus bonus, int critchance, int critmultiplier, BigDecimal dpsMultiplier,
                       BigDecimal cdMultiplier, BigDecimal cdOfDpsMultiplier, Skill unlockSkill) {
        if (bonus == null) bonus = new Bonus(0, BonusType.NULL);
        this.bonus = bonus;
        this.critchance = critchance;
        this.critmultiplier = critmultiplier;
        this.dpsMultiplier = dpsMultiplier;
        this.cdMultiplier = cdMultiplier;
        this.cdOfDpsMultiplier = cdOfDpsMultiplier;
        this.unlockSkill = unlockSkill;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public int getCritChance() {
        return critchance;
    }

    public int getCritmultiplier() {
        return critmultiplier;
    }

    public BigDecimal getDpsMultiplier() {
        return dpsMultiplier;
    }

    public BigDecimal getCdMultiplier() {
        return cdMultiplier;
    }

    public BigDecimal getCdOfDpsMultiplier() {
        return cdOfDpsMultiplier;
    }
}