package backup.discord;

import backup.Chat;
import backup.User;
import backup.adapters.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class DiscordSlashCommandMessage extends Message {
    public DiscordSlashCommandMessage(SlashCommandInteractionEvent event) {
        this.text = event.getFullCommandName();
        this.chat = new Chat(event.getChannelId(), event.getMessageChannel().getName());
        this.user = new User(event.getUser().getId(), event.getUser().getName());
        this.id = event.getId();
    }
}
