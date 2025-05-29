package com.piLovr.messengerAdapters.discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.piLovr.messengerAdapters.Listener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
public class DiscordListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Listener.onMessage(new DiscordMessage(event));
    }

    @Override
    public void onReady(ReadyEvent event) {}
    @Override
    public void onSessionInvalidate(SessionInvalidateEvent event) {}
    @Override
    public void onSessionDisconnect(SessionDisconnectEvent event) {}
    @Override
    public void onSessionResume(SessionResumeEvent event) {}
    @Override
    public void onSessionRecreate(SessionRecreateEvent event) {}
    @Override
    public void onShutdown(ShutdownEvent event) {}
}
