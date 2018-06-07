package com.under.discord.session.listener;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.domain.Session;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.event.SessionStopped;
import com.under.discord.session.discord.tool.MessageTool;
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
    private final MessageTool messageTool;

    @Autowired
    public SessionFlusher(SessionComponent sessionComponent, 
                          MessageTool messageTool) {
        this.sessionComponent = sessionComponent;
        this.messageTool = messageTool;
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
        for (SessionRecord sessionRecord : sessionRecords) {
            try {
                sessionComponent.save(sessionRecord);
                
            } catch (DataIntegrityViolationException ex) {
                if(event != null) {
                    messageTool.reply((PrivateMessageReceivedEvent) event, format("%s already registered", sessionRecord.getUser()) );
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