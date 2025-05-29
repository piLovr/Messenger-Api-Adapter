package com.piLovr.messengerAdapters.discord;

import com.piLovr.messengerAdapters.Platform;
import com.piLovr.messengerAdapters.adapters.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiscordMessage extends Message {
    public DiscordMessage(MessageReceivedEvent event){
        net.dv8tion.jda.api.entities.Message baseMessage = event.getMessage();
        this.platform = Platform.DISCORD;
        this.text = baseMessage.getContentDisplay();

        this.chatId = baseMessage.getChannelId();
        this.senderId = baseMessage.getAuthor().getId();
        this.id = event.getMessageId();

        this.pushName = baseMessage.getAuthor().getName();
        this.attachments = null; //baseMessage.getAttachments(); //type lol

        //this.quotedMessage

    }
}
