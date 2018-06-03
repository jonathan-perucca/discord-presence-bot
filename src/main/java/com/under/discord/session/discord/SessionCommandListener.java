package com.under.discord.session.discord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.under.discord.config.BotProperties;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.domain.SessionRecordStatisticsCSV;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.util.Check;
import com.under.discord.util.MessageTool;
import com.under.discord.util.Parser;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SessionCommandListener extends ListenerAdapter {

    private static final Logger logger = getLogger(SessionCommandListener.class);
    private final SessionComponent sessionComponent;
    private final Display display;
    private final CsvMapper csvMapper;
    private final BotProperties botProperties;

    @Autowired
    public SessionCommandListener(SessionComponent sessionComponent,
                                  Display display, 
                                  CsvMapper csvMapper,
                                  BotProperties botProperties) {
        this.sessionComponent = sessionComponent;
        this.display = display;
        this.csvMapper = csvMapper;
        this.botProperties = botProperties;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        List<String> adminNames = botProperties.getAdminNames();
        if( Check.authorIsBot(event) ) {
            return;
        }
        if( !Check.isAuthorized(event.getAuthor().getName(), adminNames) ) {
            MessageTool.reply(event, "You're not an admin");
            return;
        }

        String content = event.getMessage().getContent();
        if( "!session:start".equalsIgnoreCase(content) ) {
            sessionComponent.startSession();

            MessageTool.reply(event, "Session started");
        }

        if( "!session:stop".equalsIgnoreCase(content) ) {
            sessionComponent.stopSession(event);

            MessageTool.reply(event, "Session stopped");
        }

        if( "!session:all".equalsIgnoreCase(content)) {
            List<SessionRecord> sessionRecords = sessionComponent.getAllSessionRecords();
            String sessionRecordTextReport = display.recordsToText(sessionRecords);

            if(sessionRecordTextReport.trim().isEmpty()) {
                sessionRecordTextReport = "No record found";
            }

            MessageTool.reply(event, sessionRecordTextReport);
        }

        String sessionFromCommand = "!session:from";
        if( content.startsWith(sessionFromCommand) ) {
            LocalDate startDate = Parser.parseOptionAsDate(event, sessionFromCommand);
            if (startDate == null) return;
            
            MessageTool.reply(event, display.recordsToText( sessionComponent.getSessionRecordsFrom(startDate) ));
        }

        String statsCommand = "!session:stats";
        if( content.startsWith(statsCommand) ) {
            Options options = Parser.parseOptions(event, content, statsCommand);
            LocalDate startDate = Parser.parseOptionAsDate(event, options.get("from").getValue());
            if(startDate == null) return;
            
            List<SessionRecordStatistic> sessionRecordStats = sessionComponent.getSessionRecordStatsFrom(startDate);
            if(sessionRecordStats.isEmpty()) {
                MessageTool.reply(event, format("No session record found for date %s", startDate.toString()));
                return;
            }
            
            MessageTool.reply(event, display.statsToText(sessionRecordStats));
            
            // TODO : csv as an option
            // this.replyReportAsCsv(event, sessionRecordStats);
        }
    }

    private void replyReportAsCsv(PrivateMessageReceivedEvent event, List<SessionRecordStatistic> sessionRecordStats) {
        CsvSchema csvSchema = csvMapper.schemaFor(SessionRecordStatisticsCSV.class).withHeader();
        String recordsAsCSV = "No entry";

        try {
            recordsAsCSV = csvMapper.writer(csvSchema).writeValueAsString(sessionRecordStats);
        } catch (JsonProcessingException e) {
            String csvWriteErrorMessage = "Could not write CSV file";

            logger.error(csvWriteErrorMessage, e);
            MessageTool.reply(event, csvWriteErrorMessage);
            return;
        }

        PrivateChannel privateChannel = event.getMessage().getPrivateChannel();
        privateChannel.sendFile(recordsAsCSV.getBytes(), "report.txt", new MessageBuilder().append("Report done").build()).queue();
    }

    
}