package com.under.discord.util;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class Check {

    private Check() {}

    public static boolean authorIsBot(MessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }
}
