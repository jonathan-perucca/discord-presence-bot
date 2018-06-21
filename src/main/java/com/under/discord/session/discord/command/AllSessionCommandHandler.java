package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllSessionCommandHandler extends PrivateMessageCommandHandler {
    
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public AllSessionCommandHandler(SessionComponent sessionComponent,
                                    DiscordTool discordTool) {
        super(Command.builder("!session:all").build(), discordTool);
        this.command.setHelp( Help.builder(command).description("All presence list sessions").build() );
        this.sessionComponent = sessionComponent;
        this.discordTool = discordTool;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        List<SessionRecord> sessionRecords = sessionComponent.getAllSessionRecords();
        String sessionRecordTextReport = discordTool.recordsToText(sessionRecords);

        if(sessionRecordTextReport.trim().isEmpty()) {
            sessionRecordTextReport = "No record found";
        }

        discordTool.reply(event, sessionRecordTextReport);
    }
}
