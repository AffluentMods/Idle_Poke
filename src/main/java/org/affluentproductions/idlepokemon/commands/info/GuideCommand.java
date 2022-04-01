package org.affluentproductions.idlepokemon.commands.info;

import net.dv8tion.jda.api.entities.User;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.GuideUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideCommand extends BotCommand {

    public GuideCommand() {
        this.name = "guide";
        this.aliases = new String[]{"guides"};
        this.cooldown = 0.5;

    }

    @Override
    public void execute(CommandEvent e) {
        String[] args = e.getArgs();
        if (args.length == 0) {
            StringBuilder r = new StringBuilder(
                    "Use `" + Constants.PREFIX + "guide <guide>` to view one of the following guides:\n\n");
            List<String> guides = new ArrayList<>(GuideUtil.getGuides().keySet());
            for (String guide : guides) {
                guide = guide.substring(0, 1).toUpperCase() + guide.substring(1).toLowerCase();
                r.append("• ").append(guide).append(" - `").append(Constants.PREFIX).append("guide ").append(guide)
                        .append("`\n");
            }
            e.reply(MessageUtil.info("Guides", r.toString()));
            return;
        }
        StringBuilder guideName = new StringBuilder();
        for (String arg : args) guideName.append(arg).append(" ");
        if (guideName.toString().endsWith(" "))
            guideName = new StringBuilder(guideName.substring(0, guideName.length() - 1));
        String guideText = GuideUtil.getGuides().getOrDefault(guideName.toString().toLowerCase(), null);
        if (guideText == null) {
            e.reply(MessageUtil.err("Error",
                    "This guide does not exist!\nUse `" + Constants.PREFIX + "guides` to list all guides!"));
            return;
        }
        User u = e.getAuthor();
        final String finalGuideName = guideName.substring(0, 1).toUpperCase() + guideName.substring(1).toLowerCase();
        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info(finalGuideName, guideText)).queue(s -> e
                        .reply(MessageUtil.success("Guide", u.getAsMention() + " » You were just DM'd the guide!")),
                f -> e.reply(MessageUtil.err("Error",
                        u.getAsMention() + " » I can't DM you the guide! Please check DM settings and " +
                        "allow me to DM you."))), ff -> e.reply(MessageUtil.err("Error",
                u.getAsMention() + " » I can't DM you the guide! Please check DM settings and allow me to DM you.")));

    }
}