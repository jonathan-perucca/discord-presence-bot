package com.under.discord.component;

import com.under.discord.CommandConstant;
import com.under.discord.util.PatternTools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.EntityBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Component
public class VotingComponent {

    private final static String VOTE_QUESTION_PATTERN = "\\[.*";
    private final static Pattern VOTE_MESSAGE_PATTERN = Pattern.compile("\\[(.*?)\\]");
    private final static List<String> emojis = asList(
            "\uD83D\uDC7D", "\uD83D\uDE0E", "\uD83D\uDE21",
            "\uD83D\uDE0D", "\uD83D\uDCA4", "\uD83C\uDFA9",
            "\uD83E\uDD16", "\uD83D\uDC64", "\uD83D\uDCA9",
            "\uD83D\uDC27"
    );

    private final String voteCommand = CommandConstant.PREFIX + "vote";

    public String getVoteCommand() {
        return voteCommand;
    }

    public void handle(GuildMessageReceivedEvent event) {
        Message commandMessage = event.getMessage();
        String command = commandMessage.getContent();
        if(!command.startsWith(voteCommand)) {
            return;
        }

        try {
            String voteCommand = parseVoteCommand(command);
            List<String> choices = parseChoices(voteCommand);
            String question = extractQuestion(voteCommand);

            for (int i = 0; i < choices.size(); i++) {
                String choice = choices.get(i);
                String emoji = emojis.get(i);
                question += format(" (%s = %s)", choice, emoji);
            }

            event.getChannel().sendMessage(question).queue( message -> writeVoteReactions(message, event, choices) );
            commandMessage.delete().queue();

            this.sendAsEmbed(event, choices, question);
        } catch (StopExecution ex) {
            notifyMissingArgument(ex.getMessage(), event);
        }
    }

    private String parseVoteCommand(String command) {
        String[] parts = command.split(voteCommand);
        if(parts.length == 0) {
            throw new StopExecution("arguments");
        }
        return parts[1].trim();
    }

    private List<String> parseChoices(String voteMessage) {
        Optional<String> optionalChoices = PatternTools.findGroup(VOTE_MESSAGE_PATTERN, voteMessage);
        if(!optionalChoices.isPresent()) {
            throw new StopExecution("choices");
        }

        String[] choiceParts = optionalChoices.get().split(",");
        return IntStream.range(0, choiceParts.length)
                .mapToObj(String::valueOf)
                .map(value -> choiceParts[Integer.valueOf(value)].trim())
                .collect(toList());
    }

    private String extractQuestion(String voteCommand) {
        return voteCommand.replaceAll(VOTE_QUESTION_PATTERN, "").trim();
    }

    private void writeVoteReactions(Message message, GuildMessageReceivedEvent event, List<String> choices) {
        long messageId = message.getIdLong();
        for (int i = 0; i < choices.size(); i++) {
            String emoji = emojis.get(i);
            event.getChannel().addReactionById(messageId, emoji).queue();
        }
    }

    private void sendAsEmbed(GuildMessageReceivedEvent event, List<String> choices, String question) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl())
                .setThumbnail(event.getAuthor().getAvatarUrl())
                .setTitle("New vote !")
                .setTimestamp(LocalDateTime.now())
                .setFooter("footer text :)", event.getAuthor().getAvatarUrl())
                .setDescription("Description text added right here")
                .addField("Question", question, false);
        for (int i = 0; i < choices.size(); i++) {
            String choice = choices.get(i);
            String emoji = emojis.get(i);

            embedBuilder.addField(choice, emoji, true);
        }

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    private void notifyMissingArgument(String parameterMissing, GuildMessageReceivedEvent event) {
        String message = format("Vote %s missing", parameterMissing);

        event.getChannel().sendMessage(message).queue();
    }
}
