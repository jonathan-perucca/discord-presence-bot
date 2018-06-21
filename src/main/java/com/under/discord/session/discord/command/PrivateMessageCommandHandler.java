package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.session.discord.CommandHandler;
import com.under.discord.session.discord.DiscordTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public abstract class PrivateMessageCommandHandler implements CommandHandler {

    protected final Command command;
    protected final DiscordTool discordTool;

    protected PrivateMessageCommandHandler(Command command, 
                                           DiscordTool discordTool) {
        this.command = command;
        this.discordTool = discordTool;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        if( !command.validate(event.getMessage().getContent()) ) {
            return false;
        }
        
        if( !command.validateOptions(event.getMessage().getContent()) ) {
            discordTool.reply(event, "[ERROR] missing argument");
            return false;
        }
        
        return true;
    }

    @Override
    public String help() {
        return command.getHelp();
    }
}
