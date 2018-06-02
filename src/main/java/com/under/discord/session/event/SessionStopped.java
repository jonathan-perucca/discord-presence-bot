package com.under.discord.session.event;

import com.under.discord.session.domain.Session;
import lombok.Getter;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

@Getter
public class SessionStopped<T extends GenericMessageEvent> {

    private final Session session;
    private final T event;

    private SessionStopped(Session session, T event) {
        this.session = session;
        this.event = event;
    }
    
    public static <T extends GenericMessageEvent> SessionStopped with(Session session, T event) {
        return new SessionStopped<>(session, event);
    }
}