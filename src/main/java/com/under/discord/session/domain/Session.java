package com.under.discord.session.domain;

import lombok.Getter;
import net.dv8tion.jda.core.entities.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        String username = user.getName();

        userJoins.add(user);
        userTimeTrackers.putIfAbsent(username, new TimeTracker());

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
        return userTimeTrackers
                .getOrDefault(username, new TimeTracker())
                .getTotalTimeSeconds();
    }
}

