package com.under.discord.session.discord.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.tool.CsvStat;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MessageTool {

    private final CsvStat csvStat;

    @Autowired
    public MessageTool(CsvStat csvStat) {
        this.csvStat = csvStat;
    }

    public void reply(PrivateMessageReceivedEvent event, String message) {
        PrivateChannel privateChannel = event.getMessage().getPrivateChannel();
        privateChannel.sendMessage(message).queue();
    }

    public void replyAsCsv(PrivateMessageReceivedEvent event, List<SessionRecordStatistic> sessionRecordStats) {
        String recordsAsCSV;

        try {
            recordsAsCSV = csvStat.toCsvText(sessionRecordStats);
        } catch (JsonProcessingException e) {
            String csvWriteErrorMessage = "Could not write CSV file";

            log.error(csvWriteErrorMessage, e);
            this.reply(event, csvWriteErrorMessage);
            return;
        }

        PrivateChannel privateChannel = event.getMessage().getPrivateChannel();
        privateChannel.sendFile(recordsAsCSV.getBytes(), "report.csv", new MessageBuilder().append("Report done").build()).queue();
    }
}
