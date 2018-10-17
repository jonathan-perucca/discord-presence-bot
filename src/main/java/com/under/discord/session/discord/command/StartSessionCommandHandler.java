package com.under.discord.session.discord.command;

import com.under.discord.command.Command;
import com.under.discord.command.Help;
import com.under.discord.config.BotProperties;
import com.under.discord.session.SessionComponent;
import com.under.discord.session.discord.DiscordTool;
import com.under.discord.session.domain.Session;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartSessionCommandHandler extends PrivateMessageCommandHandler {

    private final SessionComponent sessionComponent;
    private final BotProperties botProperties;
    private JDA jda;

    @Autowired
    public StartSessionCommandHandler(SessionComponent sessionComponent,
                                      DiscordTool discordTool,
                                      BotProperties botProperties) {
        super( Command.builder("!session:start").build(), discordTool);
        this.botProperties = botProperties;
        this.command.setHelp( Help.builder(command).description("Start a session").build() );
        this.sessionComponent = sessionComponent;
    }

    @Autowired
    @Lazy
    public void setJda(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void apply(PrivateMessageReceivedEvent event) {
        sessionComponent.startSession();

        Guild guildToMonitor = findGuildByName(botProperties.getMonitoringGuild());
        if(guildToMonitor == null) {
            discordTool.reply(event, "Bot is not yet invited to the guild you want to monitor");
            return;
        }
        registerCurrentUsers(guildToMonitor);

        discordTool.reply(event, "Session started");
    }

    private Guild findGuildByName(String guildName) {
        return jda.getGuilds()
                .stream()
                .filter(guild -> guildName.equalsIgnoreCase(guild.getName()))
                .findFirst()
                .orElse(null);
    }

    private void registerCurrentUsers(Guild guildToMonitor) {
        for (VoiceChannel voiceChannel : guildToMonitor.getVoiceChannels()) {
            List<Member> members = voiceChannel.getMembers();
            Session currentSession = sessionComponent.getCurrentSession().get();
            for (Member member : members) {
                currentSession.declarePresence( member.getUser() );
            }
        }
    }
}
