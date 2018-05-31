package com.under.discord.session.job;

import com.under.discord.session.SessionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SessionJob {

    private final SessionComponent sessionComponent;

    @Autowired
    public SessionJob(SessionComponent sessionComponent) {
        this.sessionComponent = sessionComponent;
    }

    // TODO : set a startSession timing
//    @Scheduled(initialDelay = 2000)
    public void startSession() {
        sessionComponent.startSession();
    }

    // TODO : set a stopSession timing
//    @Scheduled(cron = "")
    public void stopSession() {
        sessionComponent.stopSession();
    }
}