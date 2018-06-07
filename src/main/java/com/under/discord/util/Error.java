package com.under.discord.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.lang.String.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Error {
    
    public static String dateFormatErrorMessage(String dateAsString) {
        return format("Argument %s is not in format yyyy-MM-dd", dateAsString);
    }
}
