package com.under.discord.session.domain;

import lombok.Getter;
import net.dv8tion.jda.core.entities.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Session {

    private final LocalDate startDate;
    private Set<User> userJoins;

    public Session(LocalDate startDate) {
        this.startDate = startDate;
        this.userJoins = new HashSet<>();
    }

    public void declarePresence(User user) {
        this.userJoins.add(user);
    }
}