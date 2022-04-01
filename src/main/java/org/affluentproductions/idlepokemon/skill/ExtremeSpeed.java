package org.affluentproductions.idlepokemon.skill;

import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.util.ClickUtil;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

public class ExtremeSpeed extends Skill {

    private static final int secondsCD = 30;

    public ExtremeSpeed() {
        super(1, "Extreme Speed", "Automatically clicks 10 clicks per second for 30 seconds",
                new SkillEffect(secondsCD * 1000) {
                    @Override
                    public void activate(Player player, boolean isBuy, boolean doubleEffect) {
                        ExtremeSpeed.run(player, isBuy, doubleEffect);
                    }

                    @Override
                    public void deactivate(Player player) {
                        ExtremeSpeed.end(player);
                    }
                }, 1, 2, 25, 10 * 60 * 1000);
    }

    public static void run(Player player, boolean isBuy, boolean doubleEffect) {
        final int val = doubleEffect ? 20 : 10;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int secondsPassed = 0;

            @Override
            public void run() {
                secondsPassed += 1;
                ClickUtil.doFullClick(player, player.getClickDamage().multiply(BigDecimal.valueOf(val)), 500);
                if (secondsPassed >= secondsCD) {
                    this.cancel();
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    public static void end(Player player) {
    }
}