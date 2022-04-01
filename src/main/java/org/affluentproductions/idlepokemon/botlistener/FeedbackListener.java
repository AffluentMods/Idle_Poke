package org.affluentproductions.idlepokemon.botlistener;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class FeedbackListener extends ListenerAdapter {

    static List<String> waitingFeedback = new ArrayList<>();

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        User author = e.getAuthor();
        if (waitingFeedback.contains(author.getId())) {
            String message = e.getMessage().getContentRaw();
            if (message.equalsIgnoreCase("done")) {
                e.getChannel().sendMessage("Thanks for your feedback!").queue();
                return;
            }
            IdlePokemon.getBot().getDatabase()
                    .update("INSERT INTO feedback VALUES ('" + author.getId() + "', '" + message + "');");
            TextChannel feedback = IdlePokemon.getHub().getTextChannelById("669694060880986152");
            if (feedback != null) {
                feedback.sendMessage("Feedback by " + author.getAsTag())
                        .queue(msg -> msg.editMessage(MessageUtil.info("Feedback", message)).queue());
            } else System.out.println("[INTERN ERR] feedback channel is null");
        }
    }
}