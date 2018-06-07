package com.under.discord.session.discord.command;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.discord.tool.Option;
import com.under.discord.session.discord.tool.Options;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.lang.String.format;

@Component
public class RemoveUserSessionCommandHandler implements CommandHandler {
    
    private final String COMMAND = "!session:remove";
    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public RemoveUserSessionCommandHandler(SessionComponent sessionComponent, 
                                           DiscordTool discordTool) {
        this.sessionComponent = sessionComponent;
        this.discordTool = discordTool;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith(COMMAND);
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        Options options = discordTool.parseOptions(event, COMMAND);
        if (!options.hasOption("user") || !options.hasOption("on")) {
            discordTool.reply(event, "Missing parameter user_name or from_date");
            return;
        }

        String username = options.get("user").getValue();
        Option onOption = options.get("on");
        LocalDate onDate = discordTool.parseOptionAsDate(event, onOption.getValue());
        if(onDate == null) return;

        if (!sessionComponent.remove(username, onDate)) {
            discordTool.reply(event, format("No record found on %s for user %s", onDate, username));
        }
        discordTool.reply(event, format("%s was removed for session on %s", username, onDate));
    }
}
