package com.piLovr.messengerAdapters.telegram;

import com.piLovr.messengerAdapters.adapters.Socket;
import com.piLovr.messengerAdapters.adapters.Message;
import com.piLovr.messengerAdapters.adapters.MessageBuilder;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramSocket extends Socket {

    private TelegramBotsLongPollingApplication listenerClient;
    private TelegramClient sendClient;
    private String token;
    public TelegramSocket(String token) {
        listenerClient = new TelegramBotsLongPollingApplication();
        this.token = token;
    }
    @Override
    public void connect() {
        try {
            listenerClient.registerBot(token, new TelegramListener(this));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to start Telegram listener client", e);
        }
        sendClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public Message sendMessage(String chatId, Message messageBuilder) {
        return null;
    }

    @Override
    public Message sendMessage(String chatId, String text) {
        return null;
    }

}
