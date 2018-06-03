package com.under.discord.session.job;

import com.under.discord.session.SessionComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class SessionJob {

    private final SessionComponent sessionComponent;

    @Autowired
    public SessionJob(SessionComponent sessionComponent) {
        this.sessionComponent = sessionComponent;
    }

    @Scheduled(cron = "${cron.session.start}")
    public void startSession() {
        log.info("Session START");
        
        sessionComponent.startSession();
    }

    @Scheduled(cron = "${cron.session.stop}")
    public void stopSession() {
        log.info("Session STOP");
        
        sessionComponent.stopSession(null);
    }
}