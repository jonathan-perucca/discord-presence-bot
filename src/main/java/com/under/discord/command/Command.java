package com.under.discord.command;

public class Command {

    private final Parser parser;
    private final String command;
    private final String help;

    public Command(CommandBuilder builder) {
        this.command = builder.command;
        this.help = builder.helper;
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

    public String help() {
        return help;
    }

    public static class CommandBuilder {
        private final String command;
        private String helper = "";

        public CommandBuilder(String command) {
            this.command = command;
        }

        public CommandBuilder helper(Help.HelpBuilder helpBuilder) {
            this.helper = helpBuilder.build().toString();
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
