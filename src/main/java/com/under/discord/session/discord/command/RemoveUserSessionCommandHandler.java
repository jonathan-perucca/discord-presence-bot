package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.command.Option;
import com.under.discord.command.Options;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class RemoveUserSessionCommandHandler extends PrivateMessageCommandHandler {
    
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public RemoveUserSessionCommandHandler(SessionComponent sessionComponent, 
                                           DiscordTool discordTool) {
        super(
                Command.builder("!session:remove")
                        .helper(
                                Help.builder("!session:remove user <user_name> on <session_date> (csv)")
                                        .description("Remove a user presence on a specific session")
                                        .example("!session:remove user under on 2018-02-10")
                                        .addOption( new Option("user").required(true) )
                                        .addOption( new Option("on").required(true) )
                                        .addOption( new Option("csv").required(false) )
                        )
                        .build()
        );
        this.sessionComponent = sessionComponent;
        this.discordTool = discordTool;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        Options options = command.parse( event.getMessage().getContent() );
        if (!options.hasOption("user") || !options.hasOption("on")) {
            discordTool.reply(event, "Missing parameter 'user' or 'on'");
            return;
        }
        String username = options.get("user").getValue();
        Option onOption = options.get("on");
        Optional<LocalDate> optionalOnValue = onOption.getValueAsLocalDate();
        if( !optionalOnValue.isPresent() ) return;
        LocalDate onDate = optionalOnValue.get();

        if (!sessionComponent.remove(username, onDate)) {
            discordTool.reply(event, format("No record found on %s for user %s", onDate, username));
        }
        
        discordTool.reply(event, format("%s was removed for session on %s", username, onDate));
    }
}
