package com.under.discord.session.discord.command;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllSessionCommandHandler implements CommandHandler {
    
    private final String COMMAND = "!session:all";
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public AllSessionCommandHandler(SessionComponent sessionComponent,
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
        List<SessionRecord> sessionRecords = sessionComponent.getAllSessionRecords();
        String sessionRecordTextReport = discordTool.recordsToText(sessionRecords);

        if(sessionRecordTextReport.trim().isEmpty()) {
            sessionRecordTextReport = "No record found";
        }

        discordTool.reply(event, sessionRecordTextReport);
    }
}
