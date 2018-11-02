package com.under.discord.session.listener;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.domain.Session;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.event.SessionStopped;
import com.under.discord.session.mapper.SessionMapper;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Component
public class SessionFlusher {

    private final SessionComponent sessionComponent;
    private final SessionMapper sessionMapper;
    private final DiscordTool discordTool;

    @Autowired
    public SessionFlusher(SessionComponent sessionComponent,
                          SessionMapper sessionMapper,
                          DiscordTool discordTool) {
        this.sessionComponent = sessionComponent;
        this.sessionMapper = sessionMapper;
        this.discordTool = discordTool;
    }

    @EventListener
    public void on(SessionStopped sessionStoppedEvent) {
        Session session = sessionStoppedEvent.getSession();
        GenericMessageEvent event = sessionStoppedEvent.getEvent();

        List<SessionRecord> sessionRecords = sessionMapper.toRecord( session, false );

        session.stop();
        PrivateMessageReceivedEvent receivedEvent = ( event != null ) ? PrivateMessageReceivedEvent.class.cast( event ) : null;
        if(receivedEvent == null) {
            for (SessionRecord sessionRecord : sessionRecords) {
                try {
                    sessionComponent.save(sessionRecord);
                } catch (RuntimeException ex) {
                }
            }
            return;
        }

        this.saveAndReply(sessionRecords, receivedEvent);
    }

    private void saveAndReply(List<SessionRecord> sessionRecords, PrivateMessageReceivedEvent receivedEvent) {
        for (SessionRecord sessionRecord : sessionRecords) {
            if( !sessionComponent.isValid(sessionRecord) ) {
                discordTool.reply(receivedEvent, format("User %s was not present enough time", sessionRecord.getUser()));
                continue;
            }

            try {
                sessionComponent.save(sessionRecord);
            } catch (DataIntegrityViolationException ex) {
                discordTool.reply(receivedEvent, format("%s already registered", sessionRecord.getUser()) );
            }
        }
    }
}