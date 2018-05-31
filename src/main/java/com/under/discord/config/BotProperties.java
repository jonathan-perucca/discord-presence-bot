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
}
