package com.under.discord.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class Parser {
    
    public static LocalDate parseOptionAsDate(PrivateMessageReceivedEvent event, String command) {
        String content = event.getMessage().getContent();
        String singleArgument = content.replace(command, "").trim();

        Optional<LocalDate> fromDate = parseToLocalDate(singleArgument);
        if(!fromDate.isPresent()) {
            String dateFormatErrorMessage = Error.dateFormatErrorMessage(singleArgument);

            MessageTool.reply(event, dateFormatErrorMessage);
            return null;
        }

        return fromDate.get();
    }

    public static Optional<LocalDate> parseToLocalDate(String dateAsString) {
        try {
            return Optional.of( LocalDate.parse(dateAsString) );
            
        } catch (DateTimeParseException ex) {
            log.error(Error.dateFormatErrorMessage(dateAsString), ex);
            return Optional.empty();
        }
    }
}
