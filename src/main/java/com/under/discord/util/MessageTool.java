package com.under.discord.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageTool {

    public static void reply(PrivateMessageReceivedEvent event, String message) {
        PrivateChannel privateChannel = event.getMessage().getPrivateChannel();
        privateChannel.sendMessage(message).queue();
    }
}
