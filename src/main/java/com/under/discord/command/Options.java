package com.under.discord.command;

import java.util.List;

public class Options {
    
    private final List<Option> options;

    public Options(List<Option> options) {
        this.options = options;
    }
    
    public boolean hasOption() {
        return options.size() > 0;
    }
    
    public boolean hasOption(String option) {
        return options.stream().anyMatch(opt -> opt.getOption().equalsIgnoreCase(option));
    }
    
    public Option get(String option) {
        return options.stream()
                .filter(opt -> opt.getOption().equalsIgnoreCase(option))
                .findAny()
                .orElse( null );
    }
}
