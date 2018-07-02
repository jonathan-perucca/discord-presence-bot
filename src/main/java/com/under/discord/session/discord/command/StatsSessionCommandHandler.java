package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.discord.tool.Error;
import com.under.discord.command.Option;
import com.under.discord.command.Options;
import com.under.discord.session.domain.SessionRecordStatistic;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class StatsSessionCommandHandler extends PrivateMessageCommandHandler {

    private final SessionComponent sessionComponent;

    @Autowired
    public StatsSessionCommandHandler(DiscordTool discordTool,
                                      SessionComponent sessionComponent) {
        super(
                Command.builder("!session:stats")
                        .addOption( new Option("from").required(true) )
                        .addOption( new Option("csv").required(false) )
                        .build(),
                discordTool);
        this.command.setHelp(
                Help.builder(command)
                        .description("Presence statistics of every users of every sessions since 'from_date'")
                        .example("!session:stats from 2018-01-01 or !session:stats from 2018-01-01 csv")
                        .build()
        );
        this.sessionComponent = sessionComponent;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        Options options = command.parse( event.getMessage().getContent() );
        Option fromOption = options.get("from");
        Optional<LocalDate> optionalStartDate = fromOption.getValueAsLocalDate();
        if(!optionalStartDate.isPresent()) {
            String errorMessage = Error.dateFormatErrorMessage( fromOption.getValue() );
            discordTool.reply(event, errorMessage);
            return;
        }

        LocalDate startDate = optionalStartDate.get();
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
