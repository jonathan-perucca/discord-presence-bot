package com.under.discord.session.discord;

import com.under.discord.session.SessionComponent;
import com.under.discord.util.MessageTool;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartSessionCommandHandler implements CommandHandler {

    private final String COMMAND = "!session:start";
    private final SessionComponent sessionComponent;
    private final MessageTool messageTool;

    @Autowired
    public StartSessionCommandHandler(SessionComponent sessionComponent,
                                      MessageTool messageTool) {
        this.sessionComponent = sessionComponent;
        this.messageTool = messageTool;
    }

    @Override
    public boolean supports(PrivateMessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith(COMMAND);
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        sessionComponent.startSession();

        messageTool.reply(event, "Session started");
    }
}
