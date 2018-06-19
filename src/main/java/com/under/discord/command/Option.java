package com.under.discord.command;

import com.under.discord.session.discord.tool.Error;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
@Getter
public class Option {

    private final String option;
    private final String value;
    private boolean required = true;

    public Option(String option) {
        this(option, null);
    }
    
    public Option(String option, String value) {
        this.option = option;
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public Option required(boolean required) {
        this.required = required;
        return this;
    }
    
    public Optional<LocalDate> getValueAsLocalDate() {
        try {
            return Optional.of( LocalDate.parse( value ) );

        } catch (DateTimeParseException ex) {
            log.error(Error.dateFormatErrorMessage( value ), ex);
            return Optional.empty();
        }
    }
}
