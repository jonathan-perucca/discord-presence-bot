package com.under.discord.session.discord;

import com.under.discord.config.BotProperties;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.util.Check;
import com.under.discord.util.MessageTool;
import com.under.discord.util.Parser;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Component
public class SessionCommandListener extends ListenerAdapter {

    private final SessionComponent sessionComponent;
    private final Display display;
    private final Parser parser;
    private final MessageTool messageTool;
    private final BotProperties botProperties;

    @Autowired
    public SessionCommandListener(SessionComponent sessionComponent,
                                  Display display,
                                  Parser parser, 
                                  MessageTool messageTool,
                                  BotProperties botProperties) {
        this.sessionComponent = sessionComponent;
        this.display = display;
        this.parser = parser;
        this.messageTool = messageTool;
        this.botProperties = botProperties;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        List<String> adminNames = botProperties.getAdminNames();
        if( Check.authorIsBot(event) ) {
            return;
        }
        if( !Check.isAuthorized(event.getAuthor().getName(), adminNames) ) {
            messageTool.reply(event, "You're not an admin");
            return;
        }

        String content = event.getMessage().getContent();
        if( "!session:start".equalsIgnoreCase(content) ) {
            sessionComponent.startSession();

            messageTool.reply(event, "Session started");
        }

        if( "!session:stop".equalsIgnoreCase(content) ) {
            sessionComponent.stopSession(event);

            messageTool.reply(event, "Session stopped");
        }

        if( "!session:all".equalsIgnoreCase(content)) {
            List<SessionRecord> sessionRecords = sessionComponent.getAllSessionRecords();
            String sessionRecordTextReport = display.recordsToText(sessionRecords);

            if(sessionRecordTextReport.trim().isEmpty()) {
                sessionRecordTextReport = "No record found";
            }

            messageTool.reply(event, sessionRecordTextReport);
        }

        String sessionFromCommand = "!session:list";
        if( content.startsWith(sessionFromCommand) ) {
            Options options = parser.parseOptions(event, content, sessionFromCommand);
            LocalDate startDate = parser.parseOptionAsDate(event, options.get("from").getValue());
            if (startDate == null) return;

            List<SessionRecord> sessionRecords = sessionComponent.getSessionRecordsFrom(startDate);
            if(sessionRecords.isEmpty()) {
                messageTool.reply(event, "No entry yet");
            }

            messageTool.reply(event, display.recordsToText(sessionRecords));
        }

        String statsCommand = "!session:stats";
        if( content.startsWith(statsCommand) ) {
            Options options = parser.parseOptions(event, content, statsCommand);
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
}