package com.under.discord.util;

import com.under.discord.session.discord.Option;
import com.under.discord.session.discord.Options;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class Parser {
    
    private final MessageTool messageTool;

    @Autowired
    public Parser(MessageTool messageTool) {
        this.messageTool = messageTool;
    }

    public LocalDate parseOptionAsDate(PrivateMessageReceivedEvent event, String content) {
        Optional<LocalDate> fromDate = parseToLocalDate(content);
        if(!fromDate.isPresent()) {
            String dateFormatErrorMessage = Error.dateFormatErrorMessage(content);

            messageTool.reply(event, dateFormatErrorMessage);
            return null;
        }

        return fromDate.get();
    }

    public Optional<LocalDate> parseToLocalDate(String dateAsString) {
        try {
            return Optional.of( LocalDate.parse(dateAsString) );
            
        } catch (DateTimeParseException ex) {
            log.error(Error.dateFormatErrorMessage(dateAsString), ex);
            return Optional.empty();
        }
    }

    public Options parseOptions(PrivateMessageReceivedEvent event, String content, String command) {
        String contentWithoutCommand = content.replace(command, "");

        List<String> optionsAndValue = Arrays.stream(contentWithoutCommand.split(" "))
                .sequential()
                .map(String::trim)
                .filter(string -> !string.isEmpty())
                .collect(toList());

        List<Option> result = new ArrayList<>( optionsAndValue.size()/2 );
        for (int i = 0; i < optionsAndValue.size(); i = i+2) {
            String optionPart = optionsAndValue.get(i);
            String valuePart = i+1 == optionsAndValue.size() ? null : optionsAndValue.get(i + 1);

            Option option = new Option(optionPart, valuePart);
            result.add( option );
        }
        
        Options options = new Options(result);
        if(!options.hasOption()) {
            messageTool.reply(event, "Missing date parameter - !session:stats 2018-01-01 for example");
        }
        return options;
    }
}
