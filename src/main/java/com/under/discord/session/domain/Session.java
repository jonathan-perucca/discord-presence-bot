package com.under.discord.session.domain;

import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.core.entities.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.under.discord.session.domain.VoiceChannelEvent.ENTER;
import static com.under.discord.session.domain.VoiceChannelEvent.LEAVE;
import static java.time.LocalDateTime.now;

@Getter
public class Session {

    private final LocalDate startDate;
    private Set<User> userJoins;
    private Map<String, TimeTracker> userTimeTrackers = new HashMap<>();

    public Session(LocalDate startDate) {
        this.startDate = startDate;
        this.userJoins = new HashSet<>();
    }

    public void declarePresence(User user) {
        this.userJoins.add(user);

        String username = user.getName();
        if (!userTimeTrackers.containsKey(username)) {
            userTimeTrackers.put(username, new TimeTracker());
        }

        TimeTracker timeTracker = userTimeTrackers.get(username);
        timeTracker.addTrackingEvent(ENTER, now());
    }

    public void declareLeave(User user) {
        String username = user.getName();
        if(!userTimeTrackers.containsKey(username)) return;

        leave( userTimeTrackers.get(username) );
    }
    
    public void stop() {
        userTimeTrackers.forEach((key, timeTracker) -> leave( timeTracker ));
    }

    private void leave(TimeTracker timeTracker) {
        timeTracker.addTrackingEvent(LEAVE, now());
    }

    public Long getSessionTimeFor(String username) {
        if (!userTimeTrackers.containsKey(username)) {
            return 0L;
        }

        return userTimeTrackers.get(username).getTotalTimeSeconds();
    }
}

@Getter
class TimeTracker {
    
    private List<UserTimeTrack> timeTracks = new ArrayList<>();

    public void addTrackingEvent(VoiceChannelEvent event, LocalDateTime dateTime) {
        UserTimeTrack userTimeTrack = UserTimeTrack.builder()
                .event(event)
                .dateTime(dateTime)
                .build();

        timeTracks.add(userTimeTrack);
    }

    public Long getTotalTimeSeconds() {
        Duration duration = Duration.ofSeconds(0);
        
        UserTimeTrack holder = null;
        for (UserTimeTrack timeTrack : timeTracks) {
            VoiceChannelEvent event = timeTrack.getEvent();
            if(event == ENTER) {
                holder = timeTrack;
                continue;
            }
            if(event == LEAVE && holder != null) {
                LocalDateTime enterDateTime = holder.getDateTime();
                LocalDateTime leaveDateTime = timeTrack.getDateTime();

                Duration timeSpent = Duration.between(enterDateTime, leaveDateTime);
                duration = duration.plus(timeSpent);
                holder = null;
            }
        }
        
        return duration.getSeconds();
    }
}

@Builder
@Getter
class UserTimeTrack {
    private VoiceChannelEvent event;
    private LocalDateTime dateTime;
}

enum VoiceChannelEvent {
    ENTER,
    LEAVE
}