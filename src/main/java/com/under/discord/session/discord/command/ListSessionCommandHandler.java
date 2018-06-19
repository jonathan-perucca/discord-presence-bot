package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.command.Option;
import com.under.discord.command.Options;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ListSessionCommandHandler extends PrivateMessageCommandHandler {
    
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public ListSessionCommandHandler(SessionComponent sessionComponent,
                                     DiscordTool discordTool) {
        super(
                Command.builder("!session:list")
                        .helper(
                                Help.builder("!session:list from <from_date>")
                                        .description("Presence list of every users of every sessions since 'from_date'")
                                        .example("!session:list from 2018-01-01")
                                        .addOption( new Option("from").required(true) )
                        )
                        .build()
        );
        this.discordTool = discordTool;
        this.sessionComponent = sessionComponent;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        Options options = command.parse( event.getMessage().getContent() );
        Option fromOption = options.get("from");
        Optional<LocalDate> optionalStartDate = fromOption.getValueAsLocalDate();
        if( !optionalStartDate.isPresent() ) return;
        LocalDate startDate = optionalStartDate.get();

        List<SessionRecord> sessionRecords = sessionComponent.getSessionRecordsFrom(startDate);
        if(sessionRecords.isEmpty()) {
            discordTool.reply(event, "No entry yet");
        }

        discordTool.reply(event, discordTool.recordsToText(sessionRecords));
    }
}
