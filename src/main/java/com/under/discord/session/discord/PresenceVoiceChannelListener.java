package com.under.discord.session.discord;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.domain.Session;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class PresenceVoiceChannelListener extends ListenerAdapter {

    private static final Logger logger = getLogger(PresenceVoiceChannelListener.class);
    private final SessionComponent sessionComponent;

    @Autowired
    public PresenceVoiceChannelListener(SessionComponent sessionComponent) {
        this.sessionComponent = sessionComponent;
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        Optional<Session> currentSession = sessionComponent.getCurrentSession();
        User user = event.getMember().getUser();
        String channelName = event.getChannelJoined().getName();
        if (!currentSession.isPresent()) {
            logger.debug("User {} entered {} with no session", user.getName(), channelName);
            return;
        }

        logger.info("User {} entered {} during session", user.getName(), channelName);
        currentSession.get().declarePresence(user);
    }
}

