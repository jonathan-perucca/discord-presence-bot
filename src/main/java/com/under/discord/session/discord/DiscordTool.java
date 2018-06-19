package com.under.discord.session.discord;

import com.under.discord.session.discord.tool.Display;
import com.under.discord.session.discord.tool.MessageTool;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscordTool {
    
    private final MessageTool messageTool;
    private final Display display;

    @Autowired
    public DiscordTool(MessageTool messageTool, 
                       Display display) {
        this.messageTool = messageTool;
        this.display = display;
    }


    public void reply(PrivateMessageReceivedEvent event, String content) {
        this.messageTool.reply(event, content);
    }

    public void replyAsCsv(PrivateMessageReceivedEvent event, List<SessionRecordStatistic> sessionRecordStats) {
        this.messageTool.replyAsCsv(event, sessionRecordStats);
    }

    public String recordsToText(List<SessionRecord> sessionRecords) {
        return display.recordsToText(sessionRecords);
    }

    public String statsToText(List<SessionRecordStatistic> sessionRecordStats) {
        return display.statsToText(sessionRecordStats);
    }
}
