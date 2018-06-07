package com.under.discord.session.discord;

import com.under.discord.config.BotProperties;
import com.under.discord.util.Check;
import com.under.discord.util.MessageTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandListener extends ListenerAdapter {

    private final List<CommandHandler> commandHandlers;
    private final MessageTool messageTool;
    private final BotProperties botProperties;

    @Autowired
    public CommandListener(List<CommandHandler> commandHandlers,
                           MessageTool messageTool,
                           BotProperties botProperties) {
        this.commandHandlers = commandHandlers;
        this.messageTool = messageTool;
        this.botProperties = botProperties;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        List<String> adminNames = botProperties.getAdminNames();
        if( Check.authorIsBot(event) ) {
            return;
        }
        if( !Check.isAuthorized(event.getAuthor().getName(), adminNames) ) {
            messageTool.reply(event, "You're not an admin");
            return;
        }

        commandHandlers.stream()
                .filter(commandHandler -> commandHandler.supports(event))
                .forEach(commandHandler -> commandHandler.apply(event));
    }
}