package com.piLovr.messengerAdapters.whatsapp;

import com.piLovr.messengerAdapters.Attachment;
import com.piLovr.messengerAdapters.Platform;
import com.piLovr.messengerAdapters.adapters.Message;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.message.model.MediaMessage;
import it.auties.whatsapp.model.message.model.MessageContainer;

public class WhatsappMessage extends Message {
    public WhatsappMessage(MessageInfo<?> messageInfo) {
        if(!(messageInfo instanceof ChatMessageInfo chatMessageInfo)){
            throw new UnsupportedMessageType(messageInfo.toJson());
        }

        this.platform = Platform.WHATSAPP;

        this.chatId = chatMessageInfo.parentJid().toString();
        this.senderId = chatMessageInfo.senderJid().toString();
        this.id = messageInfo.id();

        this.pushName = chatMessageInfo.pushName().orElse("");
        this.attachments = null;
        System.out.println(getAttachment(messageInfo.message()));
    }

    private MediaMessage<?> getAttachment (MessageContainer m){
        return switch(m.type()){
            case IMAGE -> m.imageMessage().get();
            case VIDEO -> m.videoMessage().get();
            case DOCUMENT -> m.documentMessage().get();
            case STICKER -> m.stickerMessage().get();
            case AUDIO -> m.audioMessage().get();
            default -> null;
        };
    }
}
