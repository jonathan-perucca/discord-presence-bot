package com.under.discord.session.discord;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.util.MessageTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllSessionCommandHandler implements CommandHandler {
    
    private final String COMMAND = "!session:all";
    private final SessionComponent sessionComponent;
    private final Display display;
    private final MessageTool messageTool;

    @Autowired
    public AllSessionCommandHandler(SessionComponent sessionComponent, 
                                    Display display, 
                                    MessageTool messageTool) {
        this.sessionComponent = sessionComponent;
        this.display = display;
        this.messageTool = messageTool;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith(COMMAND);
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        List<SessionRecord> sessionRecords = sessionComponent.getAllSessionRecords();
        String sessionRecordTextReport = display.recordsToText(sessionRecords);

        if(sessionRecordTextReport.trim().isEmpty()) {
            sessionRecordTextReport = "No record found";
        }

        messageTool.reply(event, sessionRecordTextReport);
    }
}
