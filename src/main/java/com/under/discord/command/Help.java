package com.under.discord.command;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Help {
    private final String command;
    private final String description;
    private final String example;
    private final List<String> mandatories;
    private final List<String> optionals;

    public Help(HelpBuilder builder) {
        this.command = builder.command;
        this.description = builder.description;
        this.example = builder.example;
        this.mandatories = builder.mandatories;
        this.optionals = builder.optionals;
    }

    public static HelpBuilder builder(String command) {
        return new HelpBuilder(command);
    }

    @Override
    public String toString() {
        return "`" + command + "` - " + description + lineSeparator() +
                ( example == null ? "" : "\texample: " + example + lineSeparator()) +
                ( mandatories.isEmpty() ? "" : "\tmandatory: " + mandatories.stream().collect(joining(",")) + lineSeparator() ) +
                ( optionals.isEmpty() ? "" : "\toptional: " + optionals.stream().collect(joining(",")) + lineSeparator());
    }

    public static class HelpBuilder {
        private final String command;
        private String example;
        private String description;
        private List<String> mandatories = new ArrayList<>();
        private List<String> optionals = new ArrayList<>();

        public HelpBuilder(String command) {
            this.command = command;
        }

        public HelpBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HelpBuilder example(String example) {
            this.example = example;
            return this;
        }

        public HelpBuilder addOption(Option option) {
            String optionName = option.getOption();

            if(option.isRequired()) {
                mandatories.add( optionName );
            } else {
                optionals.add( optionName );
            }
            return this;
        }

        public Help build() {
            return new Help(this);
        }
    }
}
