package com.under.discord.session.mapper;

import com.under.discord.session.discord.tool.DateTimeTool;
import com.under.discord.session.domain.Session;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class SessionMapper {

    private final DateTimeTool dateTimeTool;

    @Autowired
    public SessionMapper(DateTimeTool dateTimeTool) {
        this.dateTimeTool = dateTimeTool;
    }

    public List<SessionRecord> toRecord(Session session, boolean currentSessionTime) {
        LocalDate startDate = session.getStartDate();
        Set<User> userJoins = session.getUserJoins();

        return userJoins.stream()
                .map(user -> {
                    String username = user.getName();
                    Long sessionTime = currentSessionTime
                            ? session.getSessionTimeFromLastEnter(username)
                            : session.getSessionTimeFor(username);

                    return newSessionRecord(startDate, user, sessionTime);
                })
                .collect(toList());
    }

    public SessionRecordDTOs toSessionRecordDTOs(List<SessionRecord> sessionRecords) {
        SessionRecordDTOs sessionRecordDTOs = new SessionRecordDTOs();
        int userSize = sessionRecords.size();
        String sessionDate = userSize > 0
                ? sessionRecords.get(0).getStartDate().toString()
                : "";

        sessionRecordDTOs.setNumberOfUsers( userSize );
        sessionRecordDTOs.setSessionDate( sessionDate );
        List<SessionRecordDTO> records = new ArrayList<>();
        for (SessionRecord sessionRecord : sessionRecords) {
            SessionRecordDTO recordDTO = new SessionRecordDTO();
            recordDTO.setUser( sessionRecord.getUser() );
            recordDTO.setTimeSpent( dateTimeTool.getTimeSpentReadable(sessionRecord) );

            records.add(recordDTO);
        }
        sessionRecordDTOs.setRecords(records);

        return sessionRecordDTOs;
    }

    private SessionRecord newSessionRecord(LocalDate startDate, User user, Long timeSpentInSeconds) {
        return SessionRecord.builder()
                .startDate(startDate)
                .user(user.getName())
                .timeSpentInSeconds(timeSpentInSeconds)
                .build();
    }
}

