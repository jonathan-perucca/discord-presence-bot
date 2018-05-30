package com.under.discord.component;

public class StopExecution extends RuntimeException {

    public StopExecution(String message) {
        super(message);
    }
}
