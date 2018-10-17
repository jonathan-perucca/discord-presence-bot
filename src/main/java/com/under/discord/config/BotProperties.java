package com.under.discord.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static java.util.Collections.emptyList;

@Getter
@Setter
@ConfigurationProperties("bot")
public class BotProperties {

    /**
     * Bot token
     */
    private String token;

    /**
     * Admin nickname list
     */
    private List<String> adminNames = emptyList();

    /**
     * Minimum amount of time (in seconds) needed 
     * per user in order to have a validated session presence
     */
    private Long sessionTimeSeconds;

    /**
     * Discord guild name to monitor
     */
    private String monitoringGuild;

    @Override
    public String toString() {
        String separator = System.lineSeparator();

        return separator + "======= Bot configuration =======" + separator +
                "bot admins : " + String.join(",", adminNames) + separator +
                "session to stay to have a valid session (in seconds) : " + sessionTimeSeconds + separator +
                "guild monitored : " + monitoringGuild + separator;
    }
}
