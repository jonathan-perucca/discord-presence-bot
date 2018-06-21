package com.under.discord.command;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


// TODO : helper command can be grabbed from command build mandatory parameter
// TODO : helper options can be grabbed from command build option list
public class CommandTest {

    private Command command;

    @Test
    public void should_validate_command_without_options() {
        command = Command.builder("!session:test").build();

        boolean isValid = command.validate("!session:test");

        assertThat( isValid ).isTrue();
    }

    @Test
    public void should_not_validate_command_without_options() {
        command = Command.builder("!session:test").build();

        boolean isValid = command.validate("!test");

        assertThat( isValid ).isFalse();
    }

    @Test
    public void should_verify_option_absence_when_option_is_defined() {
        command = Command.builder("!session:test").build();

        Options options = command.parse("!session:test noarg");
        
        assertThat( options.hasOption("from") ).isFalse();
    }
    
    @Test
    public void should_verify_option_presense_when_option_is_defined() {
        command = Command.builder("!session:test").build();

        Options options = command.parse("!session:test csv");

        assertThat( options.hasOption("csv") ).isTrue();
    }
    
    @Test
    public void should_verify_option_absence() {
        command = Command.builder("!session:test").build();

        Options options = command.parse("!session:test");
        
        assertThat( options.hasOption() ).isFalse();
    }
    
    @Test
    public void should_validate_options_when_mandatoryOption_is_defined() {
        Option csvOption = new Option("csv").required( true );
        Option fromOption = new Option("from").required( true );
        command = Command.builder("!session:test")
                .addOption(csvOption)
                .addOption(fromOption)
                .build();

        boolean isValid = command.validateOptions("!session:test from 2018-06-04 csv");

        assertThat( isValid ).isTrue();
    }

    @Test
    public void should_not_validate_options_when_mandatoryOption_is_not_found() {
        Option csvOption = new Option("csv").required( true );
        command = Command.builder("!session:test")
                .addOption(csvOption)
                .build();

        boolean isValid = command.validateOptions("!session:test noarg");

        assertThat( isValid ).isFalse();
    }
    
    @Test
    public void should_output_helper_full_scenario() {
        Option csvOption = new Option("csv").required( false );
        Option fromOption = new Option("from").required( true );
        Option toOption = new Option("to").required( true );
        command = Command.builder("!session:test")
                .addOption(csvOption)
                .addOption(fromOption)
                .addOption(toOption)
                .build();
        Help helper = Help.builder(command)
                .description("Testing command")
                .example("Testing example")
                .build();
        command.setHelp(helper);

        String helpOutput = command.getHelp();
        
        assertThat( helpOutput ).isEqualTo(
                "`!session:test` - Testing command\n" +
                "\texample: Testing example\n" +
                "\tmandatory: from,to\n" +  
                "\toptional: csv\n"
        );
    }
}