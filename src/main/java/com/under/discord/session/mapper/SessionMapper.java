package com.under.discord.session.mapper;

import com.under.discord.session.domain.Session;
import com.under.discord.session.entity.SessionRecord;
import net.dv8tion.jda.core.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class SessionMapper {

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

    private SessionRecord newSessionRecord(LocalDate startDate, User user, Long timeSpentInSeconds) {
        return SessionRecord.builder()
                .startDate(startDate)
                .user(user.getName())
                .timeSpentInSeconds(timeSpentInSeconds)
                .build();
    }
}
