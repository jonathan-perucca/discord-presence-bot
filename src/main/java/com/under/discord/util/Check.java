package com.under.discord.util;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public final class Check {

    private Check() {}

    public static boolean authorIsBot(PrivateMessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }
}
