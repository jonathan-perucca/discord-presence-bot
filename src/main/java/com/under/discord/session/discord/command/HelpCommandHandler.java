package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelpCommandHandler extends PrivateMessageCommandHandler {

    private final DiscordTool discordTool;
    private final List<CommandHandler> commandHandlers;
    
    @Autowired
    public HelpCommandHandler(DiscordTool discordTool,
                              List<CommandHandler> commandHandlers) {
        super( Command.builder("!session:help").build() );
        this.discordTool = discordTool;
        this.commandHandlers = commandHandlers;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        StringBuilder help = new StringBuilder();
        help.append( "Manual" ).append( System.lineSeparator() );
        commandHandlers.forEach(commandHandler ->
                help.append( commandHandler.help() )
                        .append( System.lineSeparator() )
                        .append( "----------------------------------------" )
                        .append( System.lineSeparator() )
        );
        discordTool.reply(event, help.toString());
    }
}
