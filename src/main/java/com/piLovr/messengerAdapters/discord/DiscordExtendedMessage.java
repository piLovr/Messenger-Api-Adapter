package com.piLovr.messengerAdapters.discord;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import com.piLovr.messengerAdapters.attachments.Attachment;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class DiscordExtendedMessage extends ExtendedMessage {
    private DiscordSocket sock;
    private net.dv8tion.jda.api.entities.Message message;
    private TextChannel channel;

    public DiscordExtendedMessage(DiscordSocket sock, MessageReceivedEvent event){
        this.sock = sock;
        message = event.getMessage();
        parse(message);
    }

    public void parse(net.dv8tion.jda.api.entities.Message message){
        this.origin = sock.getAlias();
        this.channel = message.getChannel().asTextChannel(); //TODO: check if this is always a TextChannel
        this.user = new User(message.getAuthor().getId(), message.getAuthor().getName());
        this.chat = new Chat(message.getChannelId(), message.getChannel().getName());

        this.id = message.getId();
        this.text = message.getContentDisplay();
        //this.quotedMessage
    }

    public DiscordExtendedMessage(DiscordSocket sock, net.dv8tion.jda.api.entities.Message message){
        this.sock = sock;
        parse(message);
    }

    @Override
    public ExtendedMessage getQuotedMessage() {
        return new DiscordExtendedMessage(sock, message.getReferencedMessage());
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
    public ExtendedMessage reply(ExtendedMessage extendedMessage) {
        return sock.sendMessage(channel, extendedMessage);
    }

    @Override
    public ExtendedMessage reply(String text) {
        return sock.sendMessage(channel, text);
    }

    @Override
    public ExtendedMessage replyWithQuote(String text) {
        return sock.sendMessage(channel, text, message.getId());
    }
}
