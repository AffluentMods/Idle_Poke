package org.affluentproductions.idlepokemon.commands.admin;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.ancient.Ancient;
import org.affluentproductions.idlepokemon.ancient.Ancients;
import org.affluentproductions.idlepokemon.botlistener.CommandListener;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.entity.*;
import org.affluentproductions.idlepokemon.item.TimeLapse1;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AdminCommand extends BotCommand {

    public AdminCommand() {
        this.name = "admin";
        this.cooldown = 0.5;
        this.ownerCommand = true;
    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length == 0) {
            e.reply(MessageUtil.info("Usage", "Please use `" + Constants.PREFIX + this.name +
                                              " <maintenance | maintenance-message | shutdown | addbadge | " +
                                              "removebadge | timelapse | reset | setstage | setrival | setpokemon | " +
                                              "setcoins | " +
                                              "setruby | addcoins | addruby | removecoins | removeruby | disable | " +
                                              "enable>`."));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equals("maintenance")) {
            IdlePokemon.maintenance = !IdlePokemon.maintenance;
            String s = IdlePokemon.maintenance ? "on" : "off";
            e.reply(MessageUtil.info("Maintenance", "Turned " + s + " the maintenance mode!"));
            return;
        }
        if (arg.equals("maintenance-message")) {
            String mm = "";
            for (int i = 1; i < args.length; i++) mm += args[i] + " ";
            IdlePokemon.maintenance_message = mm;
            e.reply(MessageUtil.info("Maintenance", "Set the maintenance message to:\n\n" + mm));
            return;
        }
        if (arg.equals("addbadge")) {
            String arg1 = args[1];
            String arg2 = args[2];
            Player t = new Player(arg1);
            t.addBadge(arg2);
            e.reply(MessageUtil.success("Badge",
                    "Added " + EmoteUtil.getEmoteMention(arg2 + "_badge") + " " + arg2 + " badge to " + arg1));
            return;
        }
        if (arg.equals("removebadge")) {
            String arg1 = args[1];
            String arg2 = args[2];
            Player t = new Player(arg1);
            t.removeBadge(arg2);
            e.reply(MessageUtil.success("Badge",
                    "Removed " + EmoteUtil.getEmoteMention(arg2 + "_badge") + " " + arg2 + " badge from " + arg1));
            return;
        }
        if (arg.equals("reset")) {
            String arg1 = args[1];
            Player t = new Player(arg1);
            t.reset(true);
            e.reply(MessageUtil.success("Reset", t.getAsTag() + " was reset!"));
            return;
        }
        if (arg.equalsIgnoreCase("timelapse")) {
            String tid = args[1];
            int hours = Integer.parseInt(args[2]);
            Player t = new Player(tid);
            e.reply(MessageUtil.info("Time Lapse", "Starting " + hours + " hours Time Lapse..."));
            TimeLapse1.doTimeLapse(t, hours);
            e.reply(MessageUtil.success("Time Lapse", hours + " hours of progress damaged"));
            return;
        }
        if (arg.equalsIgnoreCase("addshiny")) {
            String tid = args[1];
            int pid = Integer.parseInt(args[2]);
            Player t = new Player(tid);
            ShinyUtil.addShiny(t, t.getPokemon(pid));
            e.reply(MessageUtil.success("Shiny", "Added 1 shiny to Pokemon #" + pid + " to " + t.getAsTag()));
            return;
        }
        if (arg.equalsIgnoreCase("removeshiny")) {
            String tid = args[1];
            int pid = Integer.parseInt(args[2]);
            Player t = new Player(tid);
            ShinyUtil.removeShiny(t, t.getPokemon(pid));
            e.reply(MessageUtil.success("Shiny", "Removed 1 shiny from Pokemon #" + pid + " from " + t.getAsTag()));
            return;
        }
        if (arg.equalsIgnoreCase("blacklist")) {
            String gid = args[1];
            IdlePokemon.getBot().getDatabase().update("INSERT INTO blacklist VALUES(?),", gid);
            CommandListener.blacklisted.add(gid);
            e.reply(MessageUtil.success("Blacklisted", "Successfully blacklisted the guild ID " + gid));
            return;
        }
        if (arg.equals("setstage")) {
            String arg1 = args[1];
            ClickUtil.preventClick(arg1, 10000);
            int arg2 = Integer.parseInt(args[2]);
            Player t = new Player(arg1);
            RivalUser tr = t.getRivalUser();
            tr.setAll(new Rival(1, arg2, tr), arg2, arg2, tr.isStayStage());
            e.reply(MessageUtil.success("Stage",
                    t.getAsTag() + " is now at stage `" + arg2 + "` (stay: " + tr.isStayStage() + ")"));
            ClickUtil.preventClick(arg1, -1);
            return;
        }
        if (arg.equals("setcoins") || arg.equals("setmoney")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.setCoins(arg2);
            e.reply(MessageUtil
                    .success("Coins", "set coins of " + arg1 + " to `" + FormatUtil.formatCommas(arg2) + "`"));
            return;
        }
        if (arg.equals("addcoins") || arg.equals("addmoney")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.addCoins(arg2);
            e.reply(MessageUtil
                    .success("Coins", t.getAsTag() + " gained `" + FormatUtil.formatCommas(arg2) + "` coins"));
            return;
        }
        if (arg.equals("removecoins") || arg.equals("removemoney")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.removeCoins(arg2);
            e.reply(MessageUtil.success("Coins", t.getAsTag() + " lost `" + FormatUtil.formatCommas(arg2) + "` coins"));
            return;
        }
        if (arg.equalsIgnoreCase("setsouls")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.setSouls(arg2);
            e.reply(MessageUtil
                    .success("Souls", t.getAsTag() + " now has `" + FormatUtil.formatCommas(arg2) + "` souls"));
            return;
        }
        if (arg.equalsIgnoreCase("addsouls")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.addSouls(arg2);
            e.reply(MessageUtil
                    .success("Souls", t.getAsTag() + " gained `" + FormatUtil.formatCommas(arg2) + "` souls"));
            return;
        }
        if (arg.equalsIgnoreCase("removesouls")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.removeSouls(arg2);
            e.reply(MessageUtil.success("Souls", t.getAsTag() + " lost `" + FormatUtil.formatCommas(arg2) + "` souls"));
            return;
        }
        if (arg.equals("setruby")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.setRubies(arg2);
            e.reply(MessageUtil
                    .success("Rubies", t.getAsTag() + " now has `" + FormatUtil.formatCommas(arg2) + "` rubies"));
            return;
        }
        if (arg.equals("addruby")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.addRubies(arg2);
            e.reply(MessageUtil
                    .success("Rubies", t.getAsTag() + " gained `" + FormatUtil.formatCommas(arg2) + "` rubies"));
            return;
        }
        if (arg.equals("removeruby")) {
            String arg1 = args[1];
            String arg2str = args[2];
            BigInteger arg2;
            if (arg2str.toLowerCase().contains("e"))
                arg2 = BigDecimal.valueOf(Double.parseDouble(arg2str)).toBigInteger();
            else arg2 = new BigInteger(arg2str);
            Player t = new Player(arg1);
            EcoUser teco = t.getEcoUser();
            teco.removeRubies(arg2);
            e.reply(MessageUtil
                    .success("Rubies", t.getAsTag() + " lost `" + FormatUtil.formatCommas(arg2) + "` rubies"));
            return;
        }
        if (arg.equalsIgnoreCase("setpokemon")) {
            String tid = args[1];
            int pid = Integer.parseInt(args[2]);
            int level = Integer.parseInt(args[3]);
            Player t = new Player(tid);
            Pokemon newPokemon = new Pokemon(pid, level);
            t.setPokemon(newPokemon);
            t.updateDPS();
            t.updateCD();
            e.reply(MessageUtil.success("Pokémon",
                    "Set " + t.getAsTag() + "'s " + newPokemon.getDisplayName() + " to Lv. " + level));
            return;
        }
        if (arg.equalsIgnoreCase("setancient")) {
            String tid = args[1];
            int aid = Integer.parseInt(args[2]);
            int lv = Integer.parseInt(args[3]);
            Ancient ancient = Ancients.getAncient(aid);
            Ancients.setAncient(tid, ancient, lv);
            e.reply(MessageUtil
                    .success("Ancient", "Set Ancient " + ancient.getName() + " of " + tid + " to Lv. " + lv));
            return;
        }
        if (arg.equals("disable")) {
            String arg1 = args[1];
            IdlePokemon.disable(arg1);
            e.reply(MessageUtil.success("Command disabled", "Disabled `" + Constants.PREFIX + arg1 + "`"));
            return;
        }
        if (arg.equals("enable")) {
            String arg1 = args[1];
            IdlePokemon.enable(arg1);
            e.reply(MessageUtil.success("Command enabled", "Enabled `" + Constants.PREFIX + arg1 + "`"));
            return;
        }
        if (arg.equals("shutdown")) {
            e.reply(MessageUtil.success("Shutdown", "Bye bye"));
            IdlePokemon.shutdown();
            return;
        }
        e.reply(MessageUtil.err("Error", "Invalid argument"));
    }
}