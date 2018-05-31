package com.under.discord.session.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionReportCreated {
    
    private final LocalDateTime startTime;
    
    public SessionReportCreated(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}