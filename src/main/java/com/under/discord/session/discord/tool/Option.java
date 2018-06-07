package com.under.discord.session.discord.tool;

import lombok.Getter;

@Getter
public class Option {

    private final String option;
    private final String value;

    public Option(String option, String value) {
        this.option = option;
        this.value = value;
    }
    
}
