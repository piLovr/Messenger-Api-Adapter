package com.piLovr.messengerAdapters.discord;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import com.piLovr.messengerAdapters.adapters.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class DiscordSlashCommandMessage extends Message {
    public DiscordSlashCommandMessage(SlashCommandInteractionEvent event) {
        this.text = event.getFullCommandName();
        this.chat = new Chat(event.getChannelId(), event.getMessageChannel().getName());
        this.user = new User(event.getUser().getId(), event.getUser().getName());
        this.id = event.getId();
    }
}
