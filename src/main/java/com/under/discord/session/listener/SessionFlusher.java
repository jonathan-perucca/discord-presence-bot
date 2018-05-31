package com.under.discord.session.listener;

import com.under.discord.session.domain.Session;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.entity.SessionRecordRepository;
import com.under.discord.session.event.SessionStopped;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class SessionFlusher {
    
    private final SessionRecordRepository sessionRecordRepository;

    @Autowired
    public SessionFlusher(SessionRecordRepository sessionRecordRepository) {
        this.sessionRecordRepository = sessionRecordRepository;
    }

    @Transactional
    @EventListener
    public void on(SessionStopped event) {
        Session session = event.getSession();
        LocalDate startDate = session.getStartDate();
        Set<User> userJoins = session.getUserJoins();

        List<SessionRecord> sessionRecords = userJoins.stream()
                .map(user -> newSessionRecord(startDate, user))
                .collect(toList());
        sessionRecordRepository.save(sessionRecords);
    }

    private SessionRecord newSessionRecord(LocalDate startDate, User user) {
        return SessionRecord.builder()
                .startDate(startDate)
                .user(user.getName())
                .build();
    }
}