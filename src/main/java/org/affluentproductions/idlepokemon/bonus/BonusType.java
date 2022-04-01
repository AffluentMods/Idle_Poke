package org.affluentproductions.idlepokemon.bonus;

import org.affluentproductions.idlepokemon.skill.Skill;

public enum BonusType {

    FULL_DAMAGE,
    CRITICAL_HIT,
    GOLD_DROP,
    GOLD_WORTH,
    SD_EXTREME_SPEED("extreme speed"),
    SD_SWORD_DANCE("sword dance"),
    SD_FOCUS_ENERGY("focus energy"),
    SD_HAPPY_HOUR("happy hour"),
    SD_PAY_DAY("pay day"),
    CLICK_DAMAGE,
    NULL;

    private String skill = null;

    BonusType() {
    }

    BonusType(String skill) {
        this.skill = skill;
    }

    public static BonusType getSkillDuration(Skill skill) {
        BonusType bt = BonusType.valueOf("SD_" + skill.getName().toUpperCase().replace(" ", "_"));
        bt.setSkill(skill);
        return bt;
    }

    private void setSkill(Skill skill) {
        this.skill = skill.getName();
    }

    public Skill getSkill() {
        return Skill.getSkill(skill);
    }
}
