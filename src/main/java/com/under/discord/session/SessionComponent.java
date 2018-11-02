package com.under.discord.session;

import com.under.discord.config.BotProperties;
import com.under.discord.session.domain.Session;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.domain.SessionTimer;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.entity.SessionRecordRepository;
import com.under.discord.session.event.SessionStopped;
import com.under.discord.session.mapper.SessionMapper;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SessionComponent {

    private static final Logger logger = getLogger(SessionComponent.class);
    private final ApplicationEventPublisher eventPublisher;
    private final SessionMapper sessionMapper;
    private final SessionRecordRepository sessionRecordRepository;
    private final BotProperties botProperties;
    private JDA jda;
    private Optional<Session> currentSession;

    @Autowired
    public SessionComponent(ApplicationEventPublisher eventPublisher,
                            SessionMapper sessionMapper,
                            SessionRecordRepository sessionRecordRepository,
                            BotProperties botProperties) {
        this.eventPublisher = eventPublisher;
        this.sessionMapper = sessionMapper;
        this.sessionRecordRepository = sessionRecordRepository;
        this.botProperties = botProperties;
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

    public List<SessionRecord> getCurrentSessionRecords() {
        Optional<Session> currentSession = getCurrentSession();
        if( !currentSession.isPresent() ) {
            return Collections.emptyList();
        }

        return sessionMapper.toRecord( currentSession.get(), true );
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
        return sessionRecordRepository.findByStartDateEqualOrAfter(fromDate)
                .stream()
                .collect( groupingBy(SessionRecord::getUser, Collectors.counting()) )
                .entrySet()
                .stream()
                .map(this::toStatistic)
                .collect(toList());
    }
    
    private SessionRecordStatistic toStatistic(Map.Entry<String, Long> entry) {
        return SessionRecordStatistic.builder()
                .user( entry.getKey() )
                .presenceTimes( String.valueOf(entry.getValue()) )
                .build();
    }

    public Session startSession() {
        if(this.currentSession.isPresent()) {
            logger.debug("Current session is already started");
            return currentSession.get();
        }
        LocalDate startDate = LocalDate.now();
        logger.info("New session started at {}", startDate);

        Session session = (this.currentSession = Optional.of(new Session(startDate, new SessionTimer()))).get();

        Guild guildToMonitor = findGuildByName(botProperties.getMonitoringGuild());
        if(guildToMonitor == null) {
            logger.debug("Bot is not yet invited to the guild ({}) you want to monitor", botProperties.getMonitoringGuild());
            return session;
        }
        registerCurrentUsers(guildToMonitor);

        return session;
    }

    private Guild findGuildByName(String guildName) {
        return jda.getGuilds()
                .stream()
                .filter(guild -> guildName.equalsIgnoreCase(guild.getName()))
                .findFirst()
                .orElse(null);
    }

    private void registerCurrentUsers(Guild guildToMonitor) {
        Session currentSession = this.currentSession.get();

        guildToMonitor.getVoiceChannels().stream()
                .map(VoiceChannel::getMembers)
                .flatMap(Collection::stream)
                .map(Member::getUser)
                .forEach(currentSession::declarePresence);
    }

    public void stopSession() {
        stopSession(null);
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

    public boolean isValid(SessionRecord sessionRecord) {
        return botProperties.getSessionTimeSeconds() <= sessionRecord.getTimeSpentInSeconds();
    }

    @Autowired
    @Lazy
    public void setJda(JDA jda) {
        this.jda = jda;
    }
}