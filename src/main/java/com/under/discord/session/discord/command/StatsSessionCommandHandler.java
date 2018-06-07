package com.under.discord.session.discord.command;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.discord.tool.Option;
import com.under.discord.session.discord.tool.Options;
import com.under.discord.session.domain.SessionRecordStatistic;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Component
public class StatsSessionCommandHandler implements CommandHandler {

    private final String COMMAND = "!session:stats";
    private final DiscordTool discordTool;
    private final SessionComponent sessionComponent;

    @Autowired
    public StatsSessionCommandHandler(DiscordTool discordTool, 
                                      SessionComponent sessionComponent) {
        this.discordTool = discordTool;
        this.sessionComponent = sessionComponent;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith(COMMAND);
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        Options options = discordTool.parseOptions(event, COMMAND);
        Option fromOption = options.get("from");
        if(fromOption == null) return;
        LocalDate startDate = discordTool.parseOptionAsDate(event, fromOption.getValue());
        if(startDate == null) return;

        List<SessionRecordStatistic> sessionRecordStats = sessionComponent.getSessionRecordStatsFrom(startDate);

        if(sessionRecordStats.isEmpty()) {
            discordTool.reply(event, format("No session record found for date %s", startDate.toString()));
            return;
        }
        if(options.hasOption("csv")) {
            discordTool.replyAsCsv(event, sessionRecordStats);
        }

        discordTool.reply(event, discordTool.statsToText(sessionRecordStats));
    }
}
