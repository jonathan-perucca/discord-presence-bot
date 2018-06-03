package com.under.discord.session.discord;

import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class Display {

    private final String SEPARATOR = "\n";

    public String recordsToText(List<SessionRecord> sessionRecords) {
        StringBuilder builder = new StringBuilder();

        Map<LocalDate, List<SessionRecord>> recordsByDate
                = sessionRecords.stream().collect( groupingBy(SessionRecord::getStartDate) );

        recordsByDate.forEach((sessionDate, sessionRecordsEntry) -> {

            builder.append( "Session of " ).append( sessionDate ).append( SEPARATOR );
            sessionRecordsEntry.forEach(sessionRecord ->
                    builder.append( sessionRecord.getUser() ).append( SEPARATOR )
            );
        });

        return builder.toString();
    }

    public String statsToText(List<SessionRecordStatistic> sessionRecordStats) {
        StringBuilder builder = new StringBuilder();

        sessionRecordStats.forEach(stat ->
                builder.append( stat.getUser() )
                        .append(" - ")
                        .append( stat.getPresenceTimes() )
                        .append( SEPARATOR )
        );

        return builder.toString();
    }
}