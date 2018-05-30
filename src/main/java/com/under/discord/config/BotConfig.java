package com.under.discord.config;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@EnableConfigurationProperties({ BotProperties.class })
public class BotConfig {

    private final Logger logger = getLogger(BotConfig.class);

    @Autowired
    private BotProperties botProperties;

    @Autowired
    private List<EventListener> botListeners;

    @Bean
    public JDA client() throws LoginException, InterruptedException, RateLimitedException {
        JDABuilder builder = new JDABuilder(AccountType.BOT)
                .setAutoReconnect(true)
                .setToken(botProperties.getToken())
                .addEventListener(botListeners.toArray());

        logListeners(botListeners);

        return builder.buildAsync();
    }

    private void logListeners(List<EventListener> botListeners) {
        botListeners.forEach(eventListener -> logger.info("{} loaded", eventListener.getClass().getSimpleName()));
    }
}
