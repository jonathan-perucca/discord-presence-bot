package com.under.discord.session.discord.tool;

import com.under.discord.session.entity.SessionRecord;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class DateTimeTool {

    public String getTimeSpentReadable(SessionRecord sessionRecord) {
        Long timeSpentInSeconds = sessionRecord.getTimeSpentInSeconds();
        return format("%d Heures - %02d minutes - %02d seconds",
                timeSpentInSeconds / 3600,
                (timeSpentInSeconds % 3600) / 60,
                (timeSpentInSeconds % 60)
        );
    }
}
