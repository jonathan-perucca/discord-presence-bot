package com.under.discord.session.domain;

import lombok.Getter;
import net.dv8tion.jda.core.entities.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Session {

    private final LocalDate startDate;
    private final SessionTimer sessionTimer;
    private Set<User> userJoins;


    public Session(LocalDate startDate, SessionTimer sessionTimer) {
        this.startDate = startDate;
        this.sessionTimer = sessionTimer;
        this.userJoins = new HashSet<>();
    }

    public void declarePresence(User user) {
        userJoins.add( user );
        sessionTimer.enter( user.getName() );
    }

    public void declareLeave(User user) {
        sessionTimer.leave( user.getName() );
    }
    
    public void stop() {
        sessionTimer.leaveAll();
    }

    public Long getSessionTimeFromLastEnter(String username) {
        return sessionTimer.getTrackedTimeFromLastEnter(username);
    }

    public Long getSessionTimeFor(String username) {
        return sessionTimer.getTrackedTime( username );
    }
}

