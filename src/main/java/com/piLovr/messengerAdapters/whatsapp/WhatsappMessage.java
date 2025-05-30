package com.piLovr.messengerAdapters.whatsapp;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.adapters.Message;
import com.piLovr.messengerAdapters.attachments.Attachment;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.MediaMessage;
import it.auties.whatsapp.model.message.model.MessageContainer;

import java.util.List;
import java.util.Set;

public class WhatsappMessage extends Message {
    private final WhatsappSocket sock;
    private Jid senderId;
    private final Jid chatId;
    private final MessageInfo<?> messageInfo;
    private final ChatMessageInfo chatMessageInfo;
    public WhatsappMessage(WhatsappSocket sock, MessageInfo<?> messageInfo) {
        this.sock = sock;
        this.messageInfo = messageInfo;
        if(!(messageInfo instanceof ChatMessageInfo)){
            throw new UnsupportedMessageType(messageInfo.toJson());
        }
        this.chatMessageInfo = (ChatMessageInfo) messageInfo;

        senderId = chatMessageInfo.senderJid();
        chatId = chatMessageInfo.chatJid();

        this.id = messageInfo.id();
    }

    private Attachment getAttachment (MessageContainer m){
        return null; //TODO
        /*
        return switch(m.type()){
            case IMAGE -> m.imageMessage().get();
            case VIDEO -> m.videoMessage().get();
            case DOCUMENT -> m.documentMessage().get();
            case STICKER -> m.stickerMessage().get();
            case AUDIO -> m.audioMessage().get();
            default -> null;
        };*/
    }

    @Override
    public Chat getChat() {
        this.chat = new Chat(chatMessageInfo.chatJid().toString(), chatMessageInfo.chatName());
        return chat;
    }

    @Override
    public User getUser() {
        this.user = new User(chatMessageInfo.senderJid().toString(), chatMessageInfo.pushName().orElse(null));
        return user;
    }

    @Override
    public Message getQuotedMessage() {
        return null;
    }

    @Override
    public List<String> getMentions() {
        return List.of();
    }

    @Override
    public List<Attachment> getAttachments() {
        return List.of();
    }

    @Override
    public Set<String> getWho() {
        return Set.of();
    }


    @Override
    public Message reply(Message message) {
        return sock.sendMessage(chatId, message);
    }

    @Override
    public Message reply(String text) {
        return sock.sendMessage(chatId, text);
    }

    @Override
    public Message replywQuote(String text) {
        return sock.sendMessage(chatId, text, messageInfo);
    }
}
