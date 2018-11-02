package com.under.discord.session.discord.tool;

import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;

@Component
public class Display {

    private final DateTimeTool dateTimeTool;
    private final String SEPARATOR = "\n";

    @Autowired
    public Display(DateTimeTool dateTimeTool) {
        this.dateTimeTool = dateTimeTool;
    }

    public String recordsToText(List<SessionRecord> sessionRecords) {
        StringBuilder builder = new StringBuilder();

        Map<LocalDate, List<SessionRecord>> recordsByDate
                = sessionRecords.stream().collect( groupingBy(SessionRecord::getStartDate) );

        recordsByDate.forEach((sessionDate, sessionRecordsEntry) -> {

            builder.append( "Session of " ).append( sessionDate ).append( SEPARATOR );
            sessionRecordsEntry.forEach(sessionRecord -> {
                String timeSpentHumanReadable = dateTimeTool.getTimeSpentReadable(sessionRecord);

                builder.append( sessionRecord.getUser() )
                        .append( " - " )
                        .append( timeSpentHumanReadable )
                        .append( SEPARATOR );
            });
        });

        return builder.append(" ").toString();
    }

    public String statsToText(List<SessionRecordStatistic> sessionRecordStats) {
        StringBuilder builder = new StringBuilder();

        sessionRecordStats.forEach(stat ->
                builder.append( stat.getUser() )
                        .append(" - ")
                        .append( stat.getPresenceTimes() )
                        .append( SEPARATOR )
        );

        return builder.append( " " ).toString();
    }
}
