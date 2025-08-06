package backup.whatsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import backup.Chat;
import backup.User;
import backup.adapters.ExtendedMessage;
import backup.attachments.Attachment;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.Message;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.standard.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class WhatsappExtendedMessage extends ExtendedMessage {
    private final WhatsappSocket sock;
    private Jid senderId;
    private Jid chatId;
    private final MessageInfo messageInfo;
    private final ChatMessageInfo chatMessageInfo;
    private it.auties.whatsapp.model.message.model.MessageContainer m;
    private Message.Type messageType;
    public WhatsappExtendedMessage(WhatsappSocket sock, MessageInfo messageInfo) {
        this.messageInfo = messageInfo;
        if(!(messageInfo instanceof ChatMessageInfo)){
            throw new UnsupportedMessageType(messageInfo.toString());
        }
        this.chatMessageInfo = (ChatMessageInfo) messageInfo;
        this.m = chatMessageInfo.message();
        this.sock = sock;
        parse(messageInfo);
        System.out.println(messageInfo.toString());
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            System.out.println(mapper.writeValueAsString(messageInfo));
        } catch (Exception e) {
            System.err.println("Failed to print messageInfo as JSON: " + e.getMessage());
        }

        //Test
        if(messageType.equals(Message.Type.IMAGE) || messageType.equals(Message.Type.VIDEO)) {
            //try {
                //ImageMessage imageMessage = (ImageMessage) messageInfo.message().content();
                //byte[] media = sock.sock.downloadMedia(imageMessage).join(); // -> Failed to download media or media: java.lang.IllegalStateException: Unexpected plaintext length
                byte[] media = sock.sock.downloadMedia(chatMessageInfo); // -> Failed to download media or media: it.auties.whatsapp.exception.RequestException: Node timed out: Node[description=receipt, attributes={id=3ACB3D861265CB26BE1A, type=server-error, to=420732472268@s.whatsapp.net}...
                System.out.println(media);
                previewMedia(media, "/home/pilovr/Messenger-Api-Adapter/test.jpeg");
                previewMedia(convertToWebp(media, messageType.equals(Message.Type.VIDEO)), "/home/pilovr/Messenger-Api-Adapter/test.webp");
                if (media == null || media.length == 0) {
                    System.err.println("Failed to download media or media is empty.");
                    return;
                }

                SimpleStickerMessageBuilder smb = new SimpleStickerMessageBuilder()
                        .media(convertToWebp(media, messageType.equals(Message.Type.VIDEO)))
                        .animated(false);

                sock.sock.sendMessage(chatMessageInfo.chatJid(), smb.build());
            //}catch (Exception e){
              //  System.err.println("Failed to download media or media: " + e.getMessage() + );
            //}
        }
    }

    public void parse(MessageInfo messageInfo) {
        this.origin = sock.getAlias();
        senderId = chatMessageInfo.senderJid();
        chatId = chatMessageInfo.chatJid();

        this.chat = new Chat(chatId.toString(), chatMessageInfo.chatName());
        this.user = new User(senderId.toString(), chatMessageInfo.pushName().orElse(null));

        messageType = m.type();

        this.text = getTextByType(messageType);

        this.id = messageInfo.id();
    }


    public static void previewMedia(byte[] media, String filename) {
        System.out.println("Saving file to: " + filename);
        if (media == null) {
            System.out.println("Media is null");
            return;
        }
        System.out.println("Media length: " + media.length);
        try {
            Files.write(Path.of(filename), media);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("File saved at: " + Path.of(filename).toString());
    }

    public static byte[] convertToWebp(byte[] jpegBytes, boolean animated) {
        Path tempInputFile;
        Path tempOutputFile;

        //create temp file
        try {
            tempInputFile = Files.createTempFile("input", ".jpeg");
            tempOutputFile = Files.createTempFile("output", ".webp");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp files", e);
        }
        //write in temp file
        try {
            Files.write(tempInputFile, jpegBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to temp input file", e);
        }
        //create output temp file

        //close output temp file

        //run exec stuff

        try {
            String toExec;
            if(animated){
                toExec = "ffmpeg -i " + tempInputFile + " -vcodec libwebp -filter:v fps=20,scale=512:512:force_original_aspect_ratio=decrease,format=rgba,pad=512:512:-1:-1:color=black@0.0 -loop 0 -ss 0 -t 5 -an -preset default -y " + tempOutputFile;
            }else {
                toExec = "cwebp " + tempInputFile + " -o " + tempOutputFile;
            }
            System.out.println("Executing: " + toExec);
            Process process = Runtime.getRuntime().exec(toExec);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Failed to convert JPEG to WebP, exit code: " + exitCode);
                throw new RuntimeException("Failed to convert JPEG to WebP, exit code: " + exitCode);
            } else {
                System.out.println("Successfully converted JPEG to WebP");
            }
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //delete input file
        try {
            byte[] outputBytes = Files.readAllBytes(tempOutputFile);
            Files.deleteIfExists(tempInputFile);
            Files.deleteIfExists(tempOutputFile);
            return outputBytes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read or delete temp files", e);
        }
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
