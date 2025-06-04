package com.piLovr.messengerAdapters.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {
    public final DiscordSocket sock;
    public DiscordListener(DiscordSocket discordSocket) {
        this.sock = discordSocket;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        sock.fireOnMessage(new DiscordExtendedMessage(sock, event));
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {}
    @Override
    public void onSessionInvalidate(@NotNull SessionInvalidateEvent event) {}
    @Override
    public void onSessionDisconnect(@NotNull SessionDisconnectEvent event) {}
    @Override
    public void onSessionResume(@NotNull SessionResumeEvent event) {}
    @Override
    public void onSessionRecreate(@NotNull SessionRecreateEvent event) {}
    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {}

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        sock.fireOnSlashCommand(new DiscordSlashCommandMessage(event));
    }
}
