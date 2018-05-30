package com.under.discord.listener;

import com.under.discord.util.Check;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoiceChannelListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if(Check.authorIsBot(event)) {
            return;
        }

        String content = event.getMessage().getContent();
        if(content.startsWith("!join")) {
            String[] parts = content.split("!join");
            if(parts.length == 0) {
                event.getChannel().sendMessage("Voice channel name missing").queue();
                return;
            }

            String voiceChannelName = parts[1].trim();
            Guild currentGuild = event.getGuild();

            AudioManager audioManager = currentGuild.getAudioManager();
            List<VoiceChannel> voiceChannels = currentGuild.getVoiceChannelsByName(voiceChannelName, false);
            if(voiceChannels.isEmpty()) {
                event.getChannel().sendMessage("No voice channel with name : " + voiceChannelName).queue();
                return;
            }
            audioManager.openAudioConnection(voiceChannels.get(0));
        }

        if(content.startsWith("!leave")) {
            String[] parts = content.split("!leave");
            if(parts.length == 0) {
                event.getChannel().sendMessage("Voice channel name missing").queue();
                return;
            }

            String voiceChannelName = parts[1].trim();

            Guild currentGuild = event.getGuild();

            AudioManager audioManager = currentGuild.getAudioManager();
            List<VoiceChannel> voiceChannels = currentGuild.getVoiceChannelsByName(voiceChannelName, false);
            if(voiceChannels.isEmpty()) {
                event.getChannel().sendMessage("No voice channel with name : " + voiceChannelName).queue();
                return;
            }

            audioManager.closeAudioConnection();
        }
    }
}
