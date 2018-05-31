package com.under.discord.util;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.util.CollectionUtils;

import java.util.List;

public final class Check {

    private Check() {}

    public static boolean authorIsBot(PrivateMessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }

    public static boolean isAuthorized(String name, List<String> authorizedNames) {
        return CollectionUtils.contains(authorizedNames.iterator(), name);
    }
}
