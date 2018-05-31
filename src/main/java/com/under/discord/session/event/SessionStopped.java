package com.under.discord.session.event;

import com.under.discord.session.domain.Session;
import lombok.Getter;

@Getter
public class SessionStopped {

    private final Session session;

    public SessionStopped(Session session) {
        this.session = session;
    }
}