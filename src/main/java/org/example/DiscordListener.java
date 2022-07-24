package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DiscordListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DiscordListener.class);

    private final Pattern regex;

    private final CommandController ctrl;

    public DiscordListener(CommandController ctrl) {
        regex = Pattern.compile("\"[^\"]+\"|\\S+");
        this.ctrl = ctrl;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent msg) {

        if (msg.getAuthor().isBot()) /* ne pas réagir à un message d'un autre bot */
            return;

        String content = msg.getMessage().getContentRaw();
        if (content.startsWith("!")) {
            Matcher m = regex.matcher(content.substring(1));
            String[] args = m.results().map(r -> r.group().replaceAll("^\"|\"$", "")).toArray(String[]::new);
            logger.info(msg.getAuthor().getAsTag() + ": " + content);
            ctrl.handleCommand(msg.getMessage(), args);
        }
    }
}
