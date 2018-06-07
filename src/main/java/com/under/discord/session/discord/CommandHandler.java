package com.under.discord.session.discord;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public interface CommandHandler {
    
    boolean supports(PrivateMessageReceivedEvent event);

    void apply(PrivateMessageReceivedEvent event);
}
