package com.under.discord.command;

import java.util.List;
import java.util.function.Function;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Help {
    private final Command command;
    private final String description;
    private final String example;

    public Help(HelpBuilder builder) {
        this.command = builder.command;
        this.description = builder.description;
        this.example = builder.example;
    }

    public static HelpBuilder builder(Command command) {
        return new HelpBuilder(command);
    }

    @Override
    public String toString() {
        Function<Option, String> getOptionName = Option::getOption;
        List<String> mandatories = command.mandatories.stream().map(getOptionName).collect(toList());
        List<String> optionals = command.optionals.stream().map(getOptionName).collect(toList());

        return "`" + command.command + "` - " + description + lineSeparator() +
                ( example == null ? "" : "\texample: " + example + lineSeparator()) +
                ( mandatories.isEmpty() ? "" : "\tmandatory: " + mandatories.stream().collect(joining(",")) + lineSeparator() ) +
                ( optionals.isEmpty() ? "" : "\toptional: " + optionals.stream().collect(joining(",")) + lineSeparator());
    }

    public static class HelpBuilder {
        private final Command command;
        private String example;
        private String description;

        public HelpBuilder(Command command) {
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

        public Help build() {
            return new Help(this);
        }
    }
}
