package com.piLovr.messengerAdapters.whatsapp;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import com.piLovr.messengerAdapters.attachments.Attachment;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.model.MessageType;

import java.util.List;
import java.util.Set;

public class WhatsappExtendedMessage extends ExtendedMessage {
    private final WhatsappSocket sock;
    private Jid senderId;
    private Jid chatId;
    private final MessageInfo<?> messageInfo;
    private final ChatMessageInfo chatMessageInfo;
    private it.auties.whatsapp.model.message.model.MessageContainer m;
    private MessageType messageType;
    public WhatsappExtendedMessage(WhatsappSocket sock, MessageInfo<?> messageInfo) {
        this.messageInfo = messageInfo;
        if(!(messageInfo instanceof ChatMessageInfo)){
            throw new UnsupportedMessageType(messageInfo.toJson());
        }
        this.chatMessageInfo = (ChatMessageInfo) messageInfo;
        this.m = chatMessageInfo.message();
        this.sock = sock;
        parse(messageInfo);
    }

    public void parse(MessageInfo<?> messageInfo) {
        this.origin = sock.getAlias();
        senderId = chatMessageInfo.senderJid();
        chatId = chatMessageInfo.chatJid();

        this.chat = new Chat(chatId.toString(), chatMessageInfo.chatName());
        this.user = new User(senderId.toString(), chatMessageInfo.pushName().orElse(null));

        messageType = m.type();

        this.text = getTextByType(messageType);

        this.id = messageInfo.id();
    }

    private String getTextByType(MessageType type) {
        return switch (type) {
            case TEXT -> m.textMessage().isPresent() ? m.textMessage().get().text() : (m.textWithNoContextMessage().isPresent() ? m.textWithNoContextMessage().get() : "");
            case IMAGE -> m.imageMessage().isPresent() ? m.imageMessage().get().caption().orElse("") : "";
            case VIDEO -> m.videoMessage().isPresent() ? m.videoMessage().get().caption().orElse("") : "";
            case DOCUMENT -> m.documentMessage().isPresent() ? m.documentMessage().get().fileName().orElse("") : "";
            //Sticker has no Text lol
            default -> "";
        };
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
    public ExtendedMessage getQuotedMessage() {
        return null;
        /*
        if (contextInfo == null || contextInfo.quotedMessage().isEmpty()) return null;
        WhatsappMessage quotedMsg = new WhatsappMessage(sock, raw);
        quotedMsg.from = new Jid(Objects.requireNonNull(contextInfo.quotedMessageChatJid().orElse(null)));
        quotedMsg.sender = new Jid(Objects.requireNonNull(contextInfo.quotedMessageSenderJid().orElse(null)));
        quotedMsg.text = getText(contextInfo.quotedMessage().orElse(null));
        quotedMsg.media = getMedia(contextInfo.quotedMessage().orElse(null));
        quotedMsg.generateTextStuff();
        return quotedMsg;
         */
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
    public ExtendedMessage reply(ExtendedMessage extendedMessage) {
        return sock.sendMessage(chatId, extendedMessage);
    }

    @Override
    public ExtendedMessage reply(String text) {
        return sock.sendMessage(chatId, text);
    }

    @Override
    public ExtendedMessage replyWithQuote(String text) {
        return sock.sendMessage(chatId, text, messageInfo);
    }
}
