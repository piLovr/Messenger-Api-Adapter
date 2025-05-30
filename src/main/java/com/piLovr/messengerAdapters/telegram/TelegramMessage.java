package com.piLovr.messengerAdapters.telegram;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.adapters.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class TelegramMessage extends Message {
    //TODO add "lazy loading" for attachments, mentions, quotedMessage
    private TelegramSocket sock;
    public TelegramMessage(TelegramSocket sock, List<Update> update){
        Update firstMessage = update.getFirst(); //TODO beten dass das die erste ist, sonst updateId
        //call other constructor for firstMessage
        TelegramMessage first = new TelegramMessage(firstMessage);
        update.removeFirst();
        for(Update u : update) {
            first.addAttachment(u);
        }
    }

    public TelegramMessage(TelegramSocket sock, Update update){
        org.telegram.telegrambots.meta.api.objects.message.Message message = update.getMessage();
        new TelegramMessage(message);
    }

    public TelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        org.telegram.telegrambots.meta.api.objects.User user = message.getFrom();
        org.telegram.telegrambots.meta.api.objects.chat.Chat chat = message.getChat();

        this.user = new User(String.valueOf(user.getId()), user.getUserName());
        this.chat = new Chat(String.valueOf(chat.getId()), chat.getTitle());

        this.id = String.valueOf(message.getMessageId());
        this.quotedMessage = new TelegramMessage(message.getReplyToMessage());
        this.mentions = null;

        /*
        "entities": [
            {
                "offset": 0,
                "url": null,
                "language": null,
                "text": "@BockiBot",
                "type": "mention",
                "length": 9,
                "user": null,
                "customEmojiId": null
            }
        ],
        */
        this.attachments = null;

    }

    private void addAttachment(Update update) {
        //this.attachments.add(new Attachment())
    }

    @Override
    public Message reply(Message message) {
        return null;
    }

    @Override
    public Message reply(String text) {
        return null;
    }
}
