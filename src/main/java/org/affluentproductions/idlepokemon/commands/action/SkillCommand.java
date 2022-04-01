package org.affluentproductions.idlepokemon.commands.action;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.skill.Skill;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.CooldownUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;
import org.affluentproductions.idlepokemon.util.StatsUtil;

import java.math.BigInteger;
import java.util.*;

public class SkillCommand extends BotCommand {

    public SkillCommand() {
        this.name = "skill";
        this.cooldown = 0.5;
        this.aliases = new String[]{"skills"};

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = new Player(uid);
        TreeMap<Integer, Skill> skills = Skill.getSkills();
        if (args.length == 0) {
            List<String> activeSkills = p.getActiveSkills();
            StringBuilder reply = new StringBuilder(
                    "• You can either use `" + Constants.PREFIX + "skill <skill>` or `" + Constants.PREFIX +
                    "skill <number>` to activate a skill. (Number is on the left of the skill.)\n\n");
            for (int ID : skills.keySet()) {
                Skill skill = skills.get(ID);
                String cooldown = activeSkills.contains(String.valueOf(ID)) ? "**Active**" : (p
                        .hasUpgrade(skill.getUnlockPok(), skill.getUnlockUpgrade()) ? cd(uid, ID,
                        skill.getCooldown()) : cd(skill.getCooldown()));
                reply.append("[`").append(ID).append("`] - ").append(skill.getName()).append(" | ")
                        .append(skill.getDescription()).append(" | ").append(cooldown).append(" | ")
                        .append(Pokemon.getPokemon(skill.getUnlockPok()).getDisplayName()).append(" Lv. ")
                        .append(skill.getUnlockLv()).append("\n");
            }
            e.reply(MessageUtil.info("Skills", reply.toString()));
            return;
        }
        StringBuilder fullArgs = new StringBuilder();
        for (String arg : args) fullArgs.append(arg).append(" ");
        if (fullArgs.toString().endsWith(" "))
            fullArgs = new StringBuilder(fullArgs.substring(0, fullArgs.length() - 1));
        String skillName = fullArgs.toString();
        Skill theSkill;
        long now = System.currentTimeMillis();
        if (skillName.equalsIgnoreCase("all")) {
            int activated = 0;
            for (Skill skill : Skill.getSkills().values()) {
                if (skill.getID() == 8 || skill.getID() == 9) continue;
                long cd = CooldownUtil.getCooldown(uid, "skill_cd_" + skill.getID());
                if (cd != -1) {
                    long diff = cd - now;
                    if (diff > 0) continue;
                }
                if (p.hasUpgrade(skill.getUnlockPok(), skill.getUnlockUpgrade())) {
                    activated++;
                    useSkill(p, skill, now);
                }
            }
            e.reply(MessageUtil.success("Skill", "Successfully activated `" + activated + "` skills!"));
            StatsUtil.setStat(uid, "skill-used",
                    new BigInteger(StatsUtil.getStat(uid, "skill-used", "0")).add(BigInteger.valueOf(activated))
                            .toString());
            return;
        }
        try {
            int sid = Integer.parseInt(skillName);
            theSkill = skills.get(sid);
        } catch (NumberFormatException ex) {
            theSkill = Skill.getSkill(skillName);
        }
        if (theSkill == null) {
            e.reply(MessageUtil.err("Error", "A skill with this ID or name does not exist!"));
            return;
        }
        if (!p.hasUpgrade(theSkill.getUnlockPok(), theSkill.getUnlockUpgrade())) {
            e.reply(MessageUtil.err("Error",
                    "You didn't unlock this skill yet!\nYou can unlock this skill with the Upgrade " +
                    theSkill.getUnlockUpgrade() + " of " +
                    Pokemon.getPokemon(theSkill.getUnlockPok()).getDisplayName() + ". Use `" + Constants.PREFIX +
                    "upgrade` for more information."));
            return;
        }
        long cd = CooldownUtil.getCooldown(uid, "skill_cd_" + theSkill.getID());
        if (cd != -1) {
            long diff = cd - now;
            if (diff > 0) {
                e.reply(MessageUtil.err("Error",
                        "This skill is still cooling down!\nYou can use it again in " + CooldownUtil.format(diff, uid) +
                        "."));
                return;
            } else {
                CooldownUtil.removeCooldown(uid, "skill_cd_" + theSkill.getID());
            }
        }
        useSkill(p, theSkill, now);
        e.reply(MessageUtil
                .success(theSkill.getName(), "Successfully activated the " + theSkill.getName() + " skill!"));
        StatsUtil.setStat(uid, "skill-used",
                new BigInteger(StatsUtil.getStat(uid, "skill-used", "0")).add(BigInteger.ONE).toString());
    }

    public static void useSkill(Player p, Skill theSkill, long now) {
        String uid = p.getUserId();
        boolean doubleEffect =
                theSkill.getEffect().canDoubleEffect() && CooldownUtil.getCooldown(uid, "skill_double") > now;
        CooldownUtil.addCooldown(uid, "skill_cd_" + theSkill.getID(), (now + theSkill.getCooldown()), true);
        if (doubleEffect) CooldownUtil.removeCooldown(uid, "skill_double");
        theSkill.getEffect().activate(p, true, doubleEffect);
        long activeTime = theSkill.getEffect().getActiveTime();
        HashMap<String, Bonus> bonuses = p.getBonuses();
        for (Bonus bonus : bonuses.values()) {
            if (bonus.getBonusType().name().startsWith("SD_")) {
                if (bonus.getBonusType().getSkill().getID() == theSkill.getID())
                    activeTime += (bonus.getValue() * 1000);
            }
        }
        if (activeTime > 0) {
            p.addActiveSkill(theSkill.getID());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    theSkill.getEffect().deactivate(p);
                    p.removeActiveSkill(theSkill.getID());
                }
            }, activeTime);
        }
    }

    private String cd(long ms) {
        return cd(null, -1, ms);
    }

    private String cd(String uid, int sid, long ms) {
        if (uid != null) {
            long theCD = CooldownUtil.getCooldown(uid, "skill_cd_" + sid);
            if (theCD != -1) {
                long diff = theCD - System.currentTimeMillis();
                if (diff > 0) {
                    return "**" + CooldownUtil.format(diff) + "**";
                }
            }
            return "**Ready**";
        }
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        while (ms >= 1000) {
            ms -= 1000;
            seconds++;
        }
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        String cd = "";
        if (hours > 0) cd += hours == 1 ? "1 hour, " : hours + " hours, ";
        if (minutes > 0) cd += minutes == 1 ? "1 minute, " : minutes + " minutes, ";
        if (seconds > 0) cd += seconds == 1 ? "1 second" : seconds + " seconds";
        if (cd.endsWith(", ")) cd = cd.substring(0, cd.length() - 2);
        return cd;
    }
}