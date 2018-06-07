package com.under.discord.session.discord.command;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.discord.tool.Options;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ListSessionCommandHandler implements CommandHandler {
    
    private final String COMMAND = "!session:list";
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public ListSessionCommandHandler(SessionComponent sessionComponent,
                                     DiscordTool discordTool) {
        this.discordTool = discordTool;
        this.sessionComponent = sessionComponent;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith(COMMAND);
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        Options options = discordTool.parseOptions(event, COMMAND);
        LocalDate startDate = discordTool.parseOptionAsDate(event, options.get("from").getValue());
        if (startDate == null) return;

        List<SessionRecord> sessionRecords = sessionComponent.getSessionRecordsFrom(startDate);
        if(sessionRecords.isEmpty()) {
            discordTool.reply(event, "No entry yet");
        }

        discordTool.reply(event, discordTool.recordsToText(sessionRecords));
    }
}
