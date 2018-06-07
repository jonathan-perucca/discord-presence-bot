package com.under.discord.session.discord.command;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartSessionCommandHandler implements CommandHandler {

    private final String COMMAND = "!session:start";
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public StartSessionCommandHandler(SessionComponent sessionComponent,
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
        sessionComponent.startSession();

        discordTool.reply(event, "Session started");
    }
}
