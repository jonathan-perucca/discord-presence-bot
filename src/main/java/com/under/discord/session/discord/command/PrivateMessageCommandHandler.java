package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.session.discord.CommandHandler;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public abstract class PrivateMessageCommandHandler implements CommandHandler {

    protected final Command command;

    protected PrivateMessageCommandHandler(Command command) {
        this.command = command;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return command.validate( event.getMessage().getContent() );
    }

    @Override
    public String help() {
        return command.help();
    }
}
