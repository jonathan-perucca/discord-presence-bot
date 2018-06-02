package com.under.discord.session.discord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.under.discord.config.BotProperties;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.domain.SessionRecordStatisticsCSV;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.util.Check;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SessionCommandListener extends ListenerAdapter {

    private static final Logger logger = getLogger(SessionCommandListener.class);
    private final SessionComponent sessionComponent;
    private final CsvMapper csvMapper;
    private final BotProperties botProperties;

    @Autowired
    public SessionCommandListener(SessionComponent sessionComponent,
                                  CsvMapper csvMapper,
                                  BotProperties botProperties) {
        this.sessionComponent = sessionComponent;
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
            this.reply(event, "You're not an admin");
            return;
        }

        String content = event.getMessage().getContent();
        if( "!session:start".equalsIgnoreCase(content) ) {
            sessionComponent.startSession();

            this.reply(event, "Session started");
        }

        if( "!session:stop".equalsIgnoreCase(content) ) {
            sessionComponent.stopSession(event);

            this.reply(event, "Session stopped");
        }

        if( "!session:all".equalsIgnoreCase(content)) {
            List<SessionRecord> sessionRecords = sessionComponent.getAllSessionRecords();
            String sessionRecordTextReport = toText(sessionRecords);

            if(sessionRecordTextReport.trim().isEmpty()) {
                sessionRecordTextReport = "No record found";
            }

            this.reply(event, sessionRecordTextReport);
        }

        String sessionFromCommand = "!session:from";
        if( content.startsWith(sessionFromCommand) ) {
            String singleArgument = content.replace(sessionFromCommand, "").trim();

            Optional<LocalDate> fromDate = parseToLocalDate(singleArgument);
            if(!fromDate.isPresent()) {
                String dateFormatErrorMessage = errorMessageDateFormat(singleArgument);

                this.reply(event, dateFormatErrorMessage);
                return;
            }

            LocalDate startDate = fromDate.get();
            this.reply(event, toText( sessionComponent.getSessionRecordsFrom(startDate) ));
        }

        String readFromCommand = "!session:stats";
        if( content.startsWith(readFromCommand) ) {
            String singleArgument = content.replace(readFromCommand, "").trim();

            Optional<LocalDate> fromDate = parseToLocalDate(singleArgument);
            if(!fromDate.isPresent()) {
                String dateFormatErrorMessage = errorMessageDateFormat(singleArgument);

                this.reply(event, dateFormatErrorMessage);
                return;
            }

            LocalDate startDate = fromDate.get();
            List<SessionRecordStatistic> sessionRecordStats = sessionComponent.getSessionRecordStatsFrom(startDate);
            if(sessionRecordStats.isEmpty()) {
                this.reply(event, format("No session record found for date %s", fromDate.toString()));
                return;
            }
            
            // TODO : csv as an option
            // this.replyReportAsCsv(event, sessionRecordStats);
        }
    }

    private void reply(PrivateMessageReceivedEvent event, String sessionRecordTextReport) {
        PrivateChannel privateChannel = event.getMessage().getPrivateChannel();
        privateChannel.sendMessage(sessionRecordTextReport).queue();
    }

    private void replyReportAsCsv(PrivateMessageReceivedEvent event, List<SessionRecordStatistic> sessionRecordStats) {
        CsvSchema csvSchema = csvMapper.schemaFor(SessionRecordStatisticsCSV.class).withHeader();
        String recordsAsCSV = "No entry";

        try {
            recordsAsCSV = csvMapper.writer(csvSchema).writeValueAsString(sessionRecordStats);
        } catch (JsonProcessingException e) {
            String csvWriteErrorMessage = "Could not write CSV file";

            logger.error(csvWriteErrorMessage, e);
            this.reply(event, csvWriteErrorMessage);
            return;
        }

        PrivateChannel privateChannel = event.getMessage().getPrivateChannel();
        privateChannel.sendFile(recordsAsCSV.getBytes(), "report.txt", new MessageBuilder().append("Report done").build()).queue();
    }

    private String toText(List<SessionRecord> sessionRecords) {
        StringBuilder builder = new StringBuilder();

        Map<LocalDate, List<SessionRecord>> recordsByDate
                = sessionRecords.stream().collect( Collectors.groupingBy(SessionRecord::getStartDate) );

        recordsByDate.forEach((sessionDate, sessionRecordsEntry) -> {

            builder.append( "Session of " ).append( sessionDate ).append( "\n" );
            sessionRecordsEntry.forEach(sessionRecord ->
                    builder.append( sessionRecord.getUser() ).append( "\n" )
            );
        });

        return builder.toString();
    }

    private Optional<LocalDate> parseToLocalDate(String dateAsString) {
        try {
            return Optional.of( LocalDate.parse(dateAsString) );
        } catch (DateTimeParseException ex) {
            logger.error(errorMessageDateFormat(dateAsString), ex);
            return Optional.empty();
        }
    }

    private String errorMessageDateFormat(String dateAsString) {
        return format("Argument %s is not in format YYYY-MM-dd", dateAsString);
    }
}