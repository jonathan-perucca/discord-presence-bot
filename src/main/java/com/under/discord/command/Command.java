package com.under.discord.command;

import java.util.ArrayList;
import java.util.List;

public class Command {

    final List<Option> mandatories;
    final List<Option> optionals;
    final String command;
    private final Parser parser;
    private Help help;

    public Command(CommandBuilder builder) {
        this.command = builder.command;
        this.mandatories = builder.mandatories;
        this.optionals = builder.optionals;
        this.parser = new Parser();
    }

    public static CommandBuilder builder(String command) {
        return new CommandBuilder(command);
    }
    
    public Options parse(String commandline) {
        return parser.parseOptions(commandline, command);
    }

    public boolean validate(String content) {
        return content.startsWith(command);
    }
    
    public boolean validateOptions(String commandline) {
        return mandatories.stream()
                .map(Option::getOption)
                .allMatch(commandline::contains);
    }

    public String getHelp() {
        return (help != null) ? help.toString() : "";
    }

    public void setHelp(Help help) {
        this.help = help;
    }

    public static class CommandBuilder {
        private final String command;
        private final List<Option> mandatories;
        private final List<Option> optionals;

        public CommandBuilder(String command) {
            this.command = command;
            this.mandatories = new ArrayList<>();
            this.optionals = new ArrayList<>();
        }

        public CommandBuilder addOption(Option option) {
            if(option.isRequired()) {
                mandatories.add( option );
            } else {
                optionals.add( option );
            }
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
