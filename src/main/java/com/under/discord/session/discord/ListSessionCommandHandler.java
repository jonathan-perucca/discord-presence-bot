package com.under.discord.session.discord;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.util.MessageTool;
import com.under.discord.util.Parser;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ListSessionCommandHandler implements CommandHandler {
    
    private final String COMMAND = "!session:list";
    private final Parser parser;
    private final SessionComponent sessionComponent;
    private final MessageTool messageTool;
    private final Display display;

    @Autowired
    public ListSessionCommandHandler(Parser parser,
                                     SessionComponent sessionComponent,
                                     MessageTool messageTool,
                                     Display display) {
        this.parser = parser;
        this.sessionComponent = sessionComponent;
        this.messageTool = messageTool;
        this.display = display;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith(COMMAND);
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        Options options = parser.parseOptions(event, COMMAND);
        LocalDate startDate = parser.parseOptionAsDate(event, options.get("from").getValue());
        if (startDate == null) return;

        List<SessionRecord> sessionRecords = sessionComponent.getSessionRecordsFrom(startDate);
        if(sessionRecords.isEmpty()) {
            messageTool.reply(event, "No entry yet");
        }

        messageTool.reply(event, display.recordsToText(sessionRecords));
    }
}
