package org.affluentproductions.idlepokemon.commands.action;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.Player;
import org.affluentproductions.idlepokemon.entity.Rival;
import org.affluentproductions.idlepokemon.entity.RivalUser;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.ClickUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

public class StageCommand extends BotCommand {

    public StageCommand() {
        this.name = "stage";
        this.cooldown = 1.5;

    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 2) {
            e.reply(MessageUtil.info("Usage", "Please use `" + Constants.PREFIX + this.name +
                                              " <stage/max> <stay | continue>`.\n\n**Stay** - You will stay on this " +
                                              "stage the whole time, even if you kill all rivals.\n\n**Continue** - " +
                                              "You will go to the stage and continue stages if you kill all rivals."));
            return;
        }
        Player p = new Player(uid);
        ClickUtil.preventClick(uid, 5000);
        RivalUser pr = p.getRivalUser();
        int maxStage = pr.getMaxStage();
        int minStage = maxStage - 15;
        int newStage;
        try {
            if (args[0].equalsIgnoreCase("max")) newStage = maxStage;
            else newStage = Integer.parseInt(args[0]);
            if (newStage < 1) {
                e.reply(MessageUtil.err("Error", "The argument `<stage>` must be bigger than `0`!"));
                ClickUtil.preventClick(uid, -1);
                return;
            }
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err("Error", "The argument `<stage>` must be a number!"));
            ClickUtil.preventClick(uid, -1);
            return;
        }
        if (newStage > maxStage) {
            e.reply(MessageUtil
                    .err("Error", "Your max. stage is `" + maxStage + "`!\nYou can't set your stage any higher!"));
            ClickUtil.preventClick(uid, -1);
            return;
        }
        if (newStage < minStage) {
            e.reply(MessageUtil
                    .err("Error", "To prevent big lags, you can't set your stage any lower than `" + minStage + "`!"));
            ClickUtil.preventClick(uid, -1);
            return;
        }
        boolean stay;
        if (args[1].equalsIgnoreCase("stay")) stay = true;
        else if (args[1].equalsIgnoreCase("continue")) stay = false;
        else {
            e.reply(MessageUtil.err("Error", "The argument `<stay | continue>` must be `stay` or `continue`!"));
            return;
        }
        pr.setAll(new Rival(1, newStage, pr), newStage, maxStage, stay);
        e.reply(MessageUtil.success("Stage", "Successfully went to Stage " + newStage + "!"));
        ClickUtil.preventClick(uid, -1);
    }
}