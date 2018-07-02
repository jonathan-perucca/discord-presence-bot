package com.under.discord.session.domain;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.under.discord.session.domain.VoiceChannelEvent.ENTER;
import static com.under.discord.session.domain.VoiceChannelEvent.LEAVE;

@Getter
public class TimeTracker {
    
    private List<UserTimeTrack> timeTracks = new ArrayList<>();

    public void addTrackingEvent(VoiceChannelEvent event, LocalDateTime dateTime) {
        UserTimeTrack userTimeTrack = UserTimeTrack.builder()
                .event(event)
                .dateTime(dateTime)
                .build();

        timeTracks.add(userTimeTrack);
    }

    public Long getTotalTimeSeconds( Optional<LocalDateTime> fromOption ) {
        Duration duration = Duration.ofSeconds(0);
        
        UserTimeTrack holder = null;
        for( UserTimeTrack timeTrack : timeTracks ) {
            VoiceChannelEvent event = timeTrack.getEvent();
            if( event == ENTER ) {
                holder = timeTrack;
                continue;
            }
            if( event == LEAVE && holder != null ) {
                LocalDateTime enterDateTime = holder.getDateTime();
                LocalDateTime leaveDateTime = timeTrack.getDateTime();

                Duration timeSpent = Duration.between(enterDateTime, leaveDateTime);
                duration = duration.plus(timeSpent);
                holder = null;
            }
        }

        boolean isLastEventAnEnter = holder != null;
        if( fromOption.isPresent() && duration.getSeconds() == 0 && isLastEventAnEnter) {
            return Duration.between( holder.getDateTime(), fromOption.get() ).getSeconds();
        }
        
        return duration.getSeconds();
    }
}
