package com.under.discord.listener;

import com.under.discord.util.Check;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ReplyWhatYouSaidListener extends ListenerAdapter {

	private final Logger logger = getLogger(ReplyWhatYouSaidListener.class);

	@Override
	public void onMessageReceived(MessageReceivedEvent messageReceivedEvent) {
		if(!supports(messageReceivedEvent)) {
			return;
		}

		String content = messageReceivedEvent.getMessage().getContent();
		String authorName = messageReceivedEvent.getAuthor().getName();
		logger.info("Received '{}' from {}", content, authorName);

		MessageChannel channel = messageReceivedEvent.getPrivateChannel();

		channel.sendMessage("You sent : " + content).queue();
	}



	private boolean supports(MessageReceivedEvent event) {
		return !Check.authorIsBot(event) && event.isFromType(ChannelType.PRIVATE);
	}
}