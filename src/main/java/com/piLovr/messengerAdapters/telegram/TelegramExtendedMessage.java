package com.piLovr.messengerAdapters.telegram;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import com.piLovr.messengerAdapters.attachments.Attachment;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class TelegramExtendedMessage extends ExtendedMessage {
    //TODO add "lazy loading" for attachments, mentions, quotedMessage
    private TelegramSocket sock;
    private List<Update> attachments; //TODO type Attachment
    private org.telegram.telegrambots.meta.api.objects.message.Message message;

    public TelegramExtendedMessage(TelegramSocket sock, List<Update> update){
        this.sock = sock;
        Update firstMessage = update.getFirst(); //TODO beten dass das die erste ist, sonst updateId

        parse(firstMessage.getMessage());
        update.removeFirst();
        attachments.addAll(update);
    }

    public TelegramExtendedMessage(TelegramSocket sock, Update update){
        this.sock = sock;
        parse(update.getMessage());
    }

    public TelegramExtendedMessage(TelegramSocket sock, org.telegram.telegrambots.meta.api.objects.message.Message message){
        this.sock = sock;
        parse(message);
    }

    private void parse(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        this.origin = sock.getAlias();
        this.message = message;
        org.telegram.telegrambots.meta.api.objects.User user = message.getFrom();
        org.telegram.telegrambots.meta.api.objects.chat.Chat chat = message.getChat();

        this.user = new User(String.valueOf(user.getId()), user.getUserName());
        this.chat = new Chat(String.valueOf(chat.getId()), chat.getTitle());

        this.id = String.valueOf(message.getMessageId());
        this.text = message.getText();
    }

    private void addAttachment(Update update) {
        //this.attachments.add(new Attachment())
    }

    @Override
    public ExtendedMessage getQuotedMessage() {
        if(this.quotedMessage != null) {
            return this.quotedMessage;
        }
        this.quotedMessage = new TelegramExtendedMessage(sock, message.getReplyToMessage());
        return this.quotedMessage;
    }

    @Override
    public List<String> getMentions() {
        if(message.getEntities() == null || message.getEntities().isEmpty()) {
            return List.of(); // No mentions found
        }
        if(this.mentions != null) {
            return this.mentions; // Already parsed mentions
        }
        this.mentions = message.getEntities().stream()
                .filter(entity -> "mention".equals(entity.getType()))
                .map(entity -> entity.getText())
                .toList(); //TODO
        return this.mentions; //TODO parse mentions from entities
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
    }

    @Override
    public List<Attachment> getAttachments() {
        return List.of(); //TODO parse attachments from message, e.g. photo, video, document
    }


    @Override
    public ExtendedMessage reply(ExtendedMessage extendedMessage) {
        return sock.sendMessage(chat.getId(), extendedMessage);
    }

    @Override
    public ExtendedMessage reply(String text) {
        return sock.sendMessage(chat.getId(), text);
    }

    @Override
    public ExtendedMessage replyWithQuote(String text) {
        return sock.sendMessage(chat.getId(), text, message.getMessageId());
    }
}
