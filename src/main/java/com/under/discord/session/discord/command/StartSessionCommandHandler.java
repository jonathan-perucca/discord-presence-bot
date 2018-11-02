package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartSessionCommandHandler extends PrivateMessageCommandHandler {

    private final SessionComponent sessionComponent;

    @Autowired
    public StartSessionCommandHandler(SessionComponent sessionComponent,
                                      DiscordTool discordTool) {
        super( Command.builder("!session:start").build(), discordTool);
        this.command.setHelp( Help.builder(command).description("Start a session").build() );
        this.sessionComponent = sessionComponent;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        sessionComponent.startSession();

        discordTool.reply(event, "Session started");
    }
}
