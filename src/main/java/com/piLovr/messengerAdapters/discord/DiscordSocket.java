package com.piLovr.messengerAdapters.discord;

import com.piLovr.messengerAdapters.Socket;
import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.Collection;
import java.util.EnumSet;

public class DiscordSocket extends Socket {
    private JDA sock;
    private final String token;

    private CommandListUpdateAction commands;

    public DiscordSocket(String token) {
        super("discord");
        this.token = token;
    }
    public DiscordSocket(String token, String alias) {
        super(alias);
        this.token = token;
    }

    @Override
    public void connect() {
        sock = JDABuilder.createLight(token, EnumSet.allOf(GatewayIntent.class))
                .addEventListeners(new DiscordListener(this))
                .build();
    }


    @Override
    public void disconnect() {
        sock.shutdown();
    }

    @Override
    public ExtendedMessage sendMessage(String chatId, ExtendedMessage extendedMessageBuilder) {
        return null;
    }

    @Override
    public ExtendedMessage sendMessage(String chatId, String text) {
        MessageChannel channel = sock.getTextChannelById(chatId);
        if(channel == null){
            return null;
        }//m.queue();
        return new DiscordExtendedMessage(this, channel.sendMessage(text).complete());
    }

    public ExtendedMessage sendMessage(MessageChannel channel, ExtendedMessage extendedMessageBuilder){
        return null; //new DiscordMessage(this, channel.sendMessage(messageBuilder.getText()).complete());
    }

    public ExtendedMessage sendMessage(MessageChannel channel, String text){
        return new DiscordExtendedMessage(this, channel.sendMessage(text).complete());
    }

    public ExtendedMessage sendMessage(MessageChannel channel, String text, String messegeReferenceId) {
        return new DiscordExtendedMessage(this, channel.sendMessage(text).setMessageReference(messegeReferenceId).complete());
    }

    public void addSlashCommand(CommandData... commands) {
        if(commands == null){
            this.commands = sock.updateCommands();
        }
        this.commands.addCommands(commands);
        this.commands.queue();
    }

    public void addSlashCommand(Collection<CommandData> commands) {
        if(commands == null){
            this.commands = sock.updateCommands();
        }
        this.commands.addCommands(commands);
        this.commands.queue();
    }
}
