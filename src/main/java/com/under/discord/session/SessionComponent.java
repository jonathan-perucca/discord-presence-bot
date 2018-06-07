package com.under.discord.session;

import com.under.discord.session.domain.Session;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.entity.SessionRecordRepository;
import com.under.discord.session.event.SessionStopped;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
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
        this.currentSession = Optional.empty();
    }

    @Transactional
    public void save(Iterable<SessionRecord> sessionRecords) {
        sessionRecordRepository.save(sessionRecords);
    }

    @Transactional
    public void save(SessionRecord sessionRecord) {
        sessionRecordRepository.save(sessionRecord);
    }

    @Transactional(readOnly = true)
    public List<SessionRecord> getAllSessionRecords() {
        return sessionRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<SessionRecord> getSessionRecordsFrom(LocalDate fromDate) {
        return sessionRecordRepository.findByStartDateEqualOrAfter(fromDate);
    }
    
    @Transactional(readOnly = true)
    public List<SessionRecordStatistic> getSessionRecordStatsFrom(LocalDate fromDate) {
        List<SessionRecord> sessionRecords = sessionRecordRepository.findByStartDateEqualOrAfter(fromDate);

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

    public void stopSession(GenericMessageEvent event) {
        if(!this.currentSession.isPresent()) {
            logger.debug("Session cannot be stopped, session was not started");
            return;
        }

        eventPublisher.publishEvent( SessionStopped.with(this.currentSession.get(), event) );
        this.currentSession = Optional.empty();
    }

    public boolean remove(String username, LocalDate onDate) {
        SessionRecord sessionRecord = sessionRecordRepository.findByUserAndStartDate(username, onDate);
        if(sessionRecord == null) {
            return false;
        }
        
        sessionRecordRepository.delete(sessionRecord);
        return true;
    }

    public Optional<Session> getCurrentSession() {
        return currentSession;
    }
}