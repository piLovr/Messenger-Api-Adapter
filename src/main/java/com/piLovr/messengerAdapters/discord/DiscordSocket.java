package com.piLovr.messengerAdapters.discord;

import com.piLovr.messengerAdapters.adapters.Socket;
import com.piLovr.messengerAdapters.adapters.Message;
import com.piLovr.messengerAdapters.adapters.MessageBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

import java.util.EnumSet;

public class DiscordSocket extends Socket {
    private JDA sock;
    private String token;

    public DiscordSocket(String token) {
        this.token = token;
        connect();
    }

    @Override
    public void connect() {
        sock = JDABuilder.createLight(token, EnumSet.of(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                ))
                .addEventListeners(new DiscordListener())
                .build();
    }


    @Override
    public void disconnect() {
        sock.shutdown();
    }

    @Override
    public Message sendMessage(String chatId, MessageBuilder messageBuilder) {
        return null;
    }

    @Override
    public Message sendMessage(String chatId, String text) {
        MessageChannel channel = sock.getTextChannelById(chatId);
        if(channel == null){
            return null;
        }
        MessageCreateAction m = channel.sendMessage(text);
        m.queue();
        return null;
    }
}
