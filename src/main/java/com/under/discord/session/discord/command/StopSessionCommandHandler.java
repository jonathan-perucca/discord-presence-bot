package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopSessionCommandHandler extends PrivateMessageCommandHandler {

    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public StopSessionCommandHandler(SessionComponent sessionComponent,
                                     DiscordTool discordTool) {
        super( Command.builder("!session:stop").build(), discordTool);
        this.command.setHelp( Help.builder(command).description("Stop a session").build() );
        this.sessionComponent = sessionComponent;
        this.discordTool = discordTool;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        sessionComponent.stopSession(event);

        discordTool.reply(event, "Session stopped");
    }
}
