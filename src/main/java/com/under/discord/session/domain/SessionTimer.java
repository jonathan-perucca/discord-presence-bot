package com.under.discord.session.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.under.discord.session.domain.VoiceChannelEvent.ENTER;
import static com.under.discord.session.domain.VoiceChannelEvent.LEAVE;
import static java.time.LocalDateTime.now;

public class SessionTimer {

    private Map<String, TimeTracker> userTimeTrackers = new HashMap<>();

    void enter(String username) {
        userTimeTrackers.putIfAbsent(username, new TimeTracker());

        TimeTracker timeTracker = userTimeTrackers.get(username);
        timeTracker.addTrackingEvent(ENTER, now());
    }

    void leave(String username) {
        if(!userTimeTrackers.containsKey(username)) return;

        leave( userTimeTrackers.get(username) );
    }

    private void leave(TimeTracker timeTracker) {
        timeTracker.addTrackingEvent(LEAVE, now());
    }

    void leaveAll() {
        userTimeTrackers.forEach((key, timeTracker) ->
                leave( timeTracker )
        );
    }

    Long getTrackedTimeFromLastEnter(String username) {
        if( !userTimeTrackers.containsKey(username) ) {
            return 0L;
        }

        return userTimeTrackers
                .get(username)
                .getTotalTimeSeconds( Optional.of(now()) );
    }

    Long getTrackedTime(String username) {
        return userTimeTrackers
                .getOrDefault(username, new TimeTracker())
                .getTotalTimeSeconds( Optional.empty() );
    }
}
