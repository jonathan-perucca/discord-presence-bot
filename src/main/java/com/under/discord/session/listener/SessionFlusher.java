package com.under.discord.session.listener;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.domain.Session;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.event.SessionStopped;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Component
public class SessionFlusher {

    private final SessionComponent sessionComponent;
    private final DiscordTool discordTool;

    @Autowired
    public SessionFlusher(SessionComponent sessionComponent,
                          DiscordTool discordTool) {
        this.sessionComponent = sessionComponent;
        this.discordTool = discordTool;
    }

    @EventListener
    public void on(SessionStopped sessionStoppedEvent) {
        Session session = sessionStoppedEvent.getSession();
        GenericMessageEvent event = sessionStoppedEvent.getEvent();
        LocalDate startDate = session.getStartDate();
        Set<User> userJoins = session.getUserJoins();

        List<SessionRecord> sessionRecords = userJoins.stream()
                .map(user -> newSessionRecord(startDate, user, session.getSessionTimeFor(user.getName())))
                .collect(toList());

        session.stop();
        PrivateMessageReceivedEvent receivedEvent = ( event != null ) ? PrivateMessageReceivedEvent.class.cast( event ) : null;
        for (SessionRecord sessionRecord : sessionRecords) {
            try {
                if( !sessionComponent.isValid(sessionRecord) ) {
                    discordTool.reply(receivedEvent, format("User %s was not present enough time", sessionRecord.getUser()));
                }
                sessionComponent.save(sessionRecord);

            } catch (DataIntegrityViolationException ex) {
                if(receivedEvent != null) {
                    discordTool.reply(receivedEvent, format("%s already registered", sessionRecord.getUser()) );
                }
            }
        }
    }

    private SessionRecord newSessionRecord(LocalDate startDate, User user, Long timeSpentInSeconds) {
        return SessionRecord.builder()
                .startDate(startDate)
                .user(user.getName())
                .timeSpentInSeconds(timeSpentInSeconds)
                .build();
    }
}