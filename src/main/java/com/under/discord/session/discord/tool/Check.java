package com.under.discord.session.discord.tool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.util.CollectionUtils;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Check {

    public static boolean authorIsBot(PrivateMessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }

    public static boolean isAuthorized(String name, List<String> authorizedNames) {
        return CollectionUtils.contains(authorizedNames.iterator(), name);
    }
}
