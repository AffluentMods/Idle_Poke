package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.bonus.Bonus;
import org.affluentproductions.idlepokemon.bonus.BonusType;
import org.affluentproductions.idlepokemon.entity.Rival;
import org.affluentproductions.idlepokemon.skill.PayDay;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;

public class Formula {

    private static final HashMap<String, HashMap<Integer, HashMap<Integer, BigInteger>>> luccache = new HashMap<>();

    public static BigInteger getLevelUpCost(BigInteger basecost, int level, int amount) {
        return getLevelUpCost(basecost, (level + 1), amount, true);
    }

    public static BigInteger getLevelUpCost(BigInteger basecost, int level, int amount, boolean abc) {
        /*
        HashMap<Integer, HashMap<Integer, BigInteger>> luc =
                luccache.getOrDefault(basecost.toString(), new HashMap<>());
        HashMap<Integer, BigInteger> luc2 = luc.getOrDefault(level, new HashMap<>());
        if (luc2.containsKey(amount)) return luc2.get(amount);
         */
        if (!abc) {
            return getLevelUpCost(basecost, level + 1, amount, true);
        }
        if (amount > 1) {
            BigInteger levelUpCosts = BigInteger.ZERO;
            for (int i = level; i < (level + amount); i++)
                levelUpCosts = levelUpCosts.add(getLevelUpCost(basecost, i, 1, true));
            return levelUpCosts;
        }
        if (level == 0) return basecost;
        BigInteger result = new BigDecimal(basecost).multiply((new BigDecimal("1.07").pow(level - 1))).toBigInteger();
        /*
        luc2.put(amount, result);
        luc.put(level, luc2);
        luccache.put(basecost.toString(), luc);
         */
        return result;
    }

    public static BigInteger getCoinReward(String userId, Rival rival, HashMap<String, Bonus> bonuses) {
        int level = rival.getLevel();
        BigInteger coinReward =
                BigDecimal.valueOf(rival.getMaxhealth()).divide(new BigDecimal("15"), RoundingMode.FLOOR)
                        .toBigInteger();
        if (level > 75) {
            coinReward = coinReward.add(BigDecimal.valueOf(Math.min(3, Math.pow(1.025, (level - 75)))).toBigInteger());
        }
        if (coinReward.compareTo(BigInteger.ONE) < 0) coinReward = BigInteger.ONE;
        for (String bonus : bonuses.keySet()) {
            Bonus theBonus = bonuses.get(bonus);
            BonusType bt = theBonus.getBonusType();
            if (bt == BonusType.GOLD_DROP) {
                coinReward = coinReward.add(BigDecimal.valueOf(theBonus.getValue()).multiply(new BigDecimal(coinReward))
                        .toBigInteger());
            }
        }
        for (String bonus : bonuses.keySet()) {
            Bonus theBonus = bonuses.get(bonus);
            BonusType bt = theBonus.getBonusType();
            if (bt == BonusType.GOLD_WORTH) {
                double value = theBonus.getValue();
                if (rival.isBoss()) {
                    value = value / 10.0;
                }
                BigInteger toAdd = new BigDecimal(coinReward).multiply(BigDecimal.valueOf(value)).toBigInteger();
                if (userId != null && PayDay.multiplier.containsKey(userId)) {
                    if (bonus.equalsIgnoreCase("pay day")) {
                        double mp = PayDay.multiplier.get(userId);
                        toAdd = toAdd.add(new BigDecimal(toAdd).multiply(BigDecimal.valueOf(mp)).toBigInteger());
                    }
                }
                coinReward = coinReward.add(toAdd);
            }
        }
        return coinReward;
    }

    public static double getMultiplier(int level) {
        double multiply = 0.0;
        if (level >= 200) {
            for (int i = 200; i <= level; i += 25) {
                multiply += 4;
            }
        }
        if (level >= 1000) {
            int multiplies = level / 1000;
            multiply += multiplies * 10;
        }
        if (multiply == 0) multiply = 1;
        return multiply;
    }

    private static final HashMap<Integer, Double> cachedRH = new HashMap<>();

    public static BigDecimal getDPSperGold(int level, BigDecimal basedps) {
        return BigDecimal.valueOf(level).multiply(basedps).multiply(BigDecimal.valueOf(getMultiplier(level)));
    }

    public static double getRivalHealth(int level, boolean isBoss) {
        double ibm = isBoss ? 10 : 1;
        double rivalHealth;
        if (cachedRH.containsKey(level)) rivalHealth = cachedRH.get(level);
        else {
            if (level <= 140) rivalHealth = 10 * ((level - 1) + Math.pow(1.55, level - 1));
            else if (level <= 500) rivalHealth = 10 * (139 + Math.pow(1.55, 139) * Math.pow(1.145, (level - 140)));
            else if (level <= 200000) {
                rivalHealth = 10 * (139 + Math.pow(1.55, 139) * Math.pow(1.145, 360) * pi(level));
            } else {
                rivalHealth = Math.pow(1.545, level - 200001) * 1.240 * Math.pow(10, 25409) + ((level - 1) * 10);
            }
        }
        if (!cachedRH.containsKey(level)) cachedRH.put(level, rivalHealth);
        return rivalHealth * ibm;
    }

    private static double pi(int level) {
        double p = 0;
        for (int i = 501; i <= level; i++) {
            p += (1.145 + 0.001 * Math.floor(i / 500.0));
        }
        return p;
    }
}