package com.piLovr.messengerAdapters.whatsapp;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import com.piLovr.messengerAdapters.attachments.Attachment;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.ContextInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.ContextualMessage;
import it.auties.whatsapp.model.message.model.MediaMessage;
import it.auties.whatsapp.model.message.model.Message;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.standard.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WhatsappExtendedMessage extends ExtendedMessage {
    private final WhatsappSocket sock;
    private Jid senderId;
    private Jid chatId;
    private final MessageInfo<?> messageInfo;
    private final ChatMessageInfo chatMessageInfo;
    private it.auties.whatsapp.model.message.model.MessageContainer m;
    private Message.Type messageType;
    public WhatsappExtendedMessage(WhatsappSocket sock, MessageInfo<?> messageInfo) {
        this.messageInfo = messageInfo;
        if(!(messageInfo instanceof ChatMessageInfo)){
            throw new UnsupportedMessageType(messageInfo.toString());
        }
        this.chatMessageInfo = (ChatMessageInfo) messageInfo;
        this.m = chatMessageInfo.message();
        this.sock = sock;
        parse(messageInfo);
        System.out.println(messageInfo.toString());
        //Test
        if(messageType.equals(Message.Type.IMAGE)){
            try {
                ImageMessage imageMessage = (ImageMessage) messageInfo.message().content();
                byte[] media = sock.sock.downloadMedia(imageMessage).join(); //-> Failed to download media or media: java.lang.IllegalStateException: Unexpected plaintext length
                //byte[] media = sock.sock.downloadMedia(chatMessageInfo).join();  -> Failed to download media or media: it.auties.whatsapp.exception.RequestException: Node timed out: Node[description=receipt, attributes={id=3ACB3D861265CB26BE1A, type=server-error, to=420732472268@s.whatsapp.net}...

                previewMedia(media, "/home/pilovr/Messenger-Api-Adapter/test.jpeg");
                previewMedia(jpegToWebp(media), "/home/pilovr/Messenger-Api-Adapter/test.webp");
                if (media == null || media.length == 0) {
                    System.err.println("Failed to download media or media is empty.");
                    return;
                }

                SimpleStickerMessageBuilder smb = new SimpleStickerMessageBuilder()
                        .media(jpegToWebp(media))
                        .animated(false);

                sock.sock.sendMessage(chatMessageInfo.chatJid(), smb.build());
            }catch (Exception e){
                System.err.println("Failed to download media or media: " + e.getMessage());
            }
        }
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


    public static void previewMedia(byte[] media, String filename) throws Exception {
        System.out.println("Saving file to: " + filename);
        if (media == null) {
            System.out.println("Media is null");
            return;
        }
        System.out.println("Media length: " + media.length);
        Files.write(Path.of(filename), media);
        System.out.println("File saved at: " + Path.of(filename).toString());
    }

    public static byte[] jpegToWebp(byte[] jpegBytes) throws Exception {
        // Register WebP plugin
        Class.forName("com.luciad.imageio.webp.WebP");

        // Read JPEG bytes
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(jpegBytes));
        if (image == null) throw new IllegalArgumentException("Invalid JPEG data");

        // Write as WebP
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "webp", out);
        return out.toByteArray();
    }
    private String getTextByType(Message.Type type) {
        /**
        ContextualMessage c = m.contentWithContext().get();
        ContextInfo cc = (ContextInfo) c.contextInfo().get();
         */
        return switch (type) {
            case TEXT -> ((TextMessage) m.content()).text();
            case IMAGE -> ((ImageMessage) m.content()).caption().orElse("");
            case VIDEO -> ((VideoOrGifMessage) m.content()).caption().orElse("");
            case DOCUMENT -> ((DocumentMessage) m.content()).caption().orElse("");
            //Sticker has no Text lol
            default -> "";
        };
        /*
        return switch (type) {
            case TEXT -> m.textMessage().isPresent() ? m.textMessage().get().text() : (m.textWithNoContextMessage().isPresent() ? m.textWithNoContextMessage().get() : "");
            case IMAGE -> m.imageMessage().isPresent() ? m.imageMessage().get().caption().orElse("") : "";
            case VIDEO -> m.videoMessage().isPresent() ? m.videoMessage().get().caption().orElse("") : "";
            case DOCUMENT -> m.documentMessage().isPresent() ? m.documentMessage().get().fileName().orElse("") : "";
            //Sticker has no Text lol
            default -> "";
        };*/
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
