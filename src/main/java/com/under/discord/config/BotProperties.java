package com.under.discord.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("bot")
public class BotProperties {

    /**
     * Bot token
     */
    private String token;
}
