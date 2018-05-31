package com.under.discord.session;

import com.under.discord.session.domain.Session;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.entity.SessionRecordRepository;
import com.under.discord.session.event.SessionStopped;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SessionComponent {

    private static final Logger logger = getLogger(SessionComponent.class);
    private final ApplicationEventPublisher eventPublisher;
    private final SessionRecordRepository sessionRecordRepository;
    private Optional<Session> currentSession;

    @Autowired
    public SessionComponent(ApplicationEventPublisher eventPublisher,
                            SessionRecordRepository sessionRecordRepository) {
        this.eventPublisher = eventPublisher;
        this.sessionRecordRepository = sessionRecordRepository;
    }

    @Transactional
    public void save(Iterable<SessionRecord> sessionRecords) {
        sessionRecordRepository.save(sessionRecords);
    }

    @Transactional(readOnly = true)
    public List<SessionRecord> getSessionRecordsFrom() {
        return sessionRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<SessionRecordStatistic> getSessionRecordsFrom(LocalDate fromDate) {
        List<SessionRecord> sessionRecords = sessionRecordRepository.findByStartDateAfter(fromDate);

        Map<String, Long> presenceMap = sessionRecords.stream()
                .collect( groupingBy(SessionRecord::getUser, Collectors.counting()) );

        return presenceMap.entrySet().stream()
                .map(this::toStatistic)
                .collect(toList());
    }

    private SessionRecordStatistic toStatistic(Map.Entry<String, Long> entry) {
        return SessionRecordStatistic.builder()
                .user(entry.getKey())
                .presenceTimes(String.valueOf(entry.getValue()))
                .build();
    }

    public Session startSession() {
        LocalDate startDate = LocalDate.now();
        logger.info("New session started at {}", startDate);

        this.currentSession = Optional.of( new Session(startDate) );

        return this.currentSession.get();
    }

    public void stopSession() {
        if(!this.currentSession.isPresent()) {
            logger.debug("Session cannot be stopped, session was not started");
            return;
        }

        eventPublisher.publishEvent( new SessionStopped(this.currentSession.get()) );
        this.currentSession = Optional.empty();
    }

    public Optional<Session> getCurrentSession() {
        return currentSession;
    }
}