package org.affluentproductions.idlepokemon.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class MentionUtil {

    public static User getUser(Message message) {
        String msg = message.getContentRaw();
        User u = null;
        String argsString = msg.contains(" ") ? msg.substring(msg.split(" ")[0].length() + 1) : null;
        if (argsString == null) return null;
        if (argsString.contains("#")) {
            int howManyHashtags = argsString.split("#").length - 1;
            if (howManyHashtags == 1) {
                String userName = argsString.split("#")[0];
                String discriminator = argsString.split("#")[1];
                if (discriminator.length() > 4) discriminator = discriminator.substring(0, 4);
                try {
                    int realdiscriminator = Integer.parseInt(discriminator);
                    StringBuilder discriminatorString = new StringBuilder(String.valueOf(realdiscriminator));
                    if (discriminatorString.length() < 4) {
                        int missing = 4 - discriminatorString.length();
                        for (int i = 0; i < missing; i++) {
                            discriminatorString.insert(0, "0");
                        }
                    }
                    Member m = message.getGuild().getMemberByTag(userName, discriminatorString.toString());
                    if (m != null) u = m.getUser();
                } catch (NumberFormatException ignored) {
                }
            }
        }
        if (message.getMentionedMembers(message.getGuild()).size() > 0)
            u = message.getMentionedMembers(message.getGuild()).get(0).getUser();
        return u;
    }
}