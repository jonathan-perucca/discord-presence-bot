package com.under.discord.listener;

import com.under.discord.component.VotingComponent;
import net.dv8tion.jda.client.events.message.group.react.GroupMessageReactionRemoveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteListener extends ListenerAdapter {

    private final VotingComponent votingComponent;

    @Autowired
    public VoteListener(VotingComponent votingComponent) {
        this.votingComponent = votingComponent;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        votingComponent.handle(event);
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        System.out.println("Message Reaction Add");
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        System.out.println("Message Reaction Update");
    }

    @Override
    public void onGroupMessageReactionRemove(GroupMessageReactionRemoveEvent event) {
        System.out.println("Message Reaction Remove");
    }
}
