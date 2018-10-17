package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrentSessionCommandHandler extends PrivateMessageCommandHandler {

    private final SessionComponent sessionComponent;

    protected CurrentSessionCommandHandler(DiscordTool discordTool,
                                           SessionComponent sessionComponent) {
        super(Command.builder("!session:current").build(), discordTool);
        this.command.setHelp( Help.builder(command).description("Get current session list").build() );
        this.sessionComponent = sessionComponent;

    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        List<SessionRecord> currentRecords = sessionComponent.getCurrentSessionRecords();
        if( currentRecords.isEmpty() ) {
            discordTool.reply(event, "No entry yet");
        }

        discordTool.reply(event, discordTool.recordsToText(currentRecords));
    }
}
