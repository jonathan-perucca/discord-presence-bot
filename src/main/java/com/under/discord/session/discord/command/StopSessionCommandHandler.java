package com.under.discord.session.discord.command;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopSessionCommandHandler implements CommandHandler {

    private final String COMMAND = "!session:stop";
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public StopSessionCommandHandler(SessionComponent sessionComponent,
                                     DiscordTool discordTool) {
        this.sessionComponent = sessionComponent;
        this.discordTool = discordTool;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith(COMMAND);
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        sessionComponent.stopSession(event);

        discordTool.reply(event, "Session stopped");
    }
}
