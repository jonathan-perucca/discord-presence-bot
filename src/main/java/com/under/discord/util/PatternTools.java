package com.under.discord.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.empty;

public final class PatternTools {

    private PatternTools() {}

    public static Optional<String> findGroup(Pattern pattern, String message) {
        Matcher matcher = pattern.matcher(message);

        return matcher.find() ? Optional.of(matcher.group(1)) : empty();
    }
}
