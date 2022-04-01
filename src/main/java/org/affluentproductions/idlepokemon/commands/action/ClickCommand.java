package org.affluentproductions.idlepokemon.commands.action;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.click.Click;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Pokemon;
import org.affluentproductions.idlepokemon.entity.Rival;
import org.affluentproductions.idlepokemon.entity.RivalUser;
import org.affluentproductions.idlepokemon.event.action.ClickEvent;
import org.affluentproductions.idlepokemon.manager.EventManager;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.*;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ClickCommand extends BotCommand {

    public ClickCommand() {
        this.name = "click";
        this.cooldown = 1.5;

    }

    public static boolean log = false;

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (ClickUtil.isPreventClick(uid)) {
            e.reply(MessageUtil.err("Error", "You are in a Time Lapse!\nYou can't click until it's finished."));
            return;
        }
        if (log) System.out.println("Click Log 2");
        Player p = new Player(uid);
        if (log) System.out.println("Click Log 3");
        RivalUser pr = p.getRivalUser();
        if (log) System.out.println("Click Log 4");
        Rival rival = pr.getRival();
        if (log) System.out.println("Click Log 5");
        //
        HashMap<String, Bonus> bonuses = p.getBonuses();
        if (log) System.out.println("Click Log 6");
        Click click = doClick(p, pr, rival, bonuses, p.getClickDamage(), 1);
        if (log) System.out.println("Click Log 7");
        rival = click.getRival();
        boolean killed = click.isKill();
        //
        final double newHealth = rival.getHealth();
        final double dmg = click.getDamage();
        String damage = FormatUtil.formatAbbreviated(String.valueOf(dmg), 5);
        if (damage.length() > 15) damage = damage.substring(0, 15);
        EmbedBuilder eb = new EmbedBuilder();
        if (rival.isBoss()) {
            eb.setTitle("Stage " + pr.getStage() + " | " + rival.getName() + " Boss");
            String secs = String.valueOf(((rival.getSpawned() + 30000) - System.currentTimeMillis()) / 1000.0);
            if (secs.length() > 6) secs = secs.substring(0, 6);
            eb.setFooter(secs + " seconds left");
        } else eb.setTitle("Stage " + pr.getStage() + " | " + rival.getName() + " Rival " + rival.getLevel() + "/" +
                           rival.getMaxLevel());
        BigInteger coinReward = Formula.getCoinReward(uid, rival, bonuses);
        final String rb = rival.isBoss() ? "boss" : "rival";
        if (killed) {
            eb.setDescription("You dealt `" + damage + "` damage!");
            eb.appendDescription("\nYou killed the " + rb + "!\n+ " + EmoteUtil.getCoin() + " `" +
                                 FormatUtil.formatAbbreviated(coinReward) + "`");
        } else {
            String hpbar = getHPBar(rival.getHealth(), rival.getMaxhealth());
            eb.setDescription("HP: " + hpbar + "\nYou dealt `" + damage + "` damage!");
            BigDecimal bdNewHealth = new BigDecimal(newHealth);
            String healthLeft = FormatUtil.formatAbbreviated(bdNewHealth.toBigInteger().toString(), 4);
            eb.appendDescription("\n`" + healthLeft + "` health left!\nThis " + rb + " will drop `" +
                                 FormatUtil.formatAbbreviated(coinReward) + "` " + EmoteUtil.getCoin());
        }
        if (log) System.out.println("Click Log 8");
        eb.setColor(new Color(Integer.valueOf("FF", 16), Integer.valueOf("DE", 16), Integer.valueOf("0F", 16)));
        e.reply(eb.build());
        EventManager.callEvent(new ClickEvent(p, click));
        if (log) System.out.println("Click Log 9");
    }

    private static final Random random = new Random();

    public static Click doClick(final Player p, final RivalUser pr, final Rival rival,
                                final HashMap<String, Bonus> bonuses, final BigDecimal damage, int maxRounds) {
        BigDecimal cdamage = damage;
        BigDecimal damageDealt = new BigDecimal("0");
        BigDecimal theDamage = cdamage;
        BigDecimal theHealth = BigDecimal.valueOf(rival.getHealth());
        Rival currentRival = rival;
        final int oldStage = (int) (long) pr.getStage();
        int stage = pr.getStage();
        int level = currentRival.getLevel();
        int crit = p.getCritChance();
        double critMultiplier = p.getCritMultiplier();
        for (String bonusName : bonuses.keySet()) {
            Bonus theBonus = bonuses.get(bonusName);
            BonusType bt = theBonus.getBonusType();
            if (bt == BonusType.CRITICAL_HIT) {
                crit += crit * theBonus.getValue();
            }
        }
        BigInteger coinRewards = BigInteger.ZERO;
        int rounds = 0;
        BigDecimal ZERO = BigDecimal.ZERO;
        while (theDamage.compareTo(ZERO) > 0) {
            rounds++;
            if (rounds > maxRounds) break;
            if (random.nextInt(101) <= crit) {
                cdamage = cdamage.multiply(BigDecimal.valueOf(critMultiplier));
            }
            final BigDecimal oldHealth = theHealth;
            theHealth = theHealth.subtract(cdamage);
            if (theHealth.compareTo(ZERO) < 0) theHealth = new BigDecimal("0");
            BigDecimal damageDeal = oldHealth.subtract(theHealth);
            damageDealt = damageDealt.add(damageDeal);
            theDamage = theDamage.subtract(damageDeal);
            currentRival.setHealth(theHealth.doubleValue());
            boolean killed = false;
            if (theHealth.compareTo(ZERO) <= 0) killed = true;
            if (killed) {
                if (ShinyUtil.receiveShiny(p.getUserId(), stage)) {
                    ShinyUtil.setLastStage(p.getUserId(), stage);
                    ShinyUtil.addShiny(p, Pokemon.getRandomOwnedPokemon(p.getPokemonUser()));
                }
                BigInteger coinReward = Formula.getCoinReward(p.getUserId(), currentRival, bonuses);
                List<Bonus> coinGainUpgrades = p.getUpgrades(BonusType.GOLD_DROP);
                for (Bonus ub : coinGainUpgrades) {
                    if (ub.getBonusType() == BonusType.GOLD_DROP) {
                        double mp = ub.getValue();
                        coinReward = coinReward.add(coinReward.multiply(new BigDecimal(mp).toBigInteger()));
                    }
                }
                coinRewards = coinRewards.add(coinReward);
                if (currentRival.getLevel() >= currentRival.getMaxLevel()) {
                    stage++;
                    level = 1;
                } else {
                    level++;
                }
                currentRival = new Rival(level, pr.isStayStage() ? oldStage : stage, pr);
                theHealth = BigDecimal.valueOf(currentRival.getHealth());
            }
        }
        int nextStage = currentRival.getStage();
        if (pr.isStayStage()) nextStage = oldStage;
        pr.setAll(currentRival, nextStage, pr.isStayStage());
        if (coinRewards.compareTo(BigInteger.ZERO) > 0) p.getEcoUser().addCoins(coinRewards);
        return new Click(p, rival, currentRival, damageDealt.doubleValue());
    }

    private String getHPBar(double health, double maxhealth) {
        String hpbar;
        double percentage = (health / maxhealth) * 100.0;
        String gl = EmoteUtil.getEmoteMention("HP_Green_Left");
        String gc = EmoteUtil.getEmoteMention("HP_Green_Center");
        String gr = EmoteUtil.getEmoteMention("HP_Green_Right");
        String yl = EmoteUtil.getEmoteMention("HP_Yellow_Left");
        String yc = EmoteUtil.getEmoteMention("HP_Yellow_Center");
        String rl = EmoteUtil.getEmoteMention("HP_Red_Left");
        String ec = EmoteUtil.getEmoteMention("HP_Empty_Center");
        String er = EmoteUtil.getEmoteMention("HP_Empty_Right");
        if (percentage >= 67) hpbar = gl + gc + gr;
        else if (percentage >= 34) hpbar = yl + yc + er;
        else hpbar = rl + ec + er;
        return hpbar;
    }
}