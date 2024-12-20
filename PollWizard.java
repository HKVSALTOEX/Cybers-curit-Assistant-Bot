import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.security.auth.login.LoginException;
import java.util.List;

public class PollWizard extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault("YOUR_BOT_TOKEN");
        builder.addEventListeners(new PollWizard());
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (content.startsWith("!sondage")) {
            MessageChannel channel = event.getChannel();
            String[] parts = content.split(" ", 2);
            if (parts.length < 2) return;

            String[] options = parts[1].split(",");
            if (options.length > 10) {
                channel.sendMessage("Vous ne pouvez pas avoir plus de 10 options.").queue();
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Sondage : ").append(parts[1].trim()).append("\n");
            for (int i = 0; i < options.length; i++) {
                sb.append((char) (65 + i)).append(": ").append(options[i].trim()).append("\n");
            }

            channel.sendMessage(sb.toString()).queue(messageToSend -> {
                for (int i = 0; i < options.length; i++) {
                    messageToSend.addReaction((char) (65 + i) + "\uFE0F\u20E3").queue();
                }
            });
        }
    }
}