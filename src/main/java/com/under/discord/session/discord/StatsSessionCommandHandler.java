package com.under.discord.session.discord;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.util.MessageTool;
import com.under.discord.util.Parser;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Component
public class StatsSessionCommandHandler implements CommandHandler {

    private final String COMMAND = "!session:stats";
    private final Parser parser;
    private final MessageTool messageTool;
    private final SessionComponent sessionComponent;
    private final Display display;

    @Autowired
    public StatsSessionCommandHandler(Parser parser, 
                                      MessageTool messageTool, 
                                      SessionComponent sessionComponent, 
                                      Display display) {
        this.parser = parser;
        this.messageTool = messageTool;
        this.sessionComponent = sessionComponent;
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
        if(startDate == null) return;

        List<SessionRecordStatistic> sessionRecordStats = sessionComponent.getSessionRecordStatsFrom(startDate);

        if(sessionRecordStats.isEmpty()) {
            messageTool.reply(event, format("No session record found for date %s", startDate.toString()));
            return;
        }
        if(options.hasOption("csv")) {
            messageTool.replyAsCsv(event, sessionRecordStats);
        }

        messageTool.reply(event, display.statsToText(sessionRecordStats));
    }
}
