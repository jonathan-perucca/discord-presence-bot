package com.under.discord.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class Parser {
    
    public Options parseOptions(String content, String command) {
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

        return new Options(result);
    }
}
