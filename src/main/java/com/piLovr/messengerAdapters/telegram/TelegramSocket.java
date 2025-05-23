package com.piLovr.messengerAdapters.telegram;

import com.piLovr.messengerAdapters.Socket;
import com.piLovr.messengerAdapters.adapters.Message;
import com.piLovr.messengerAdapters.adapters.MessageBuilder;

public class TelegramSocket extends Socket {
    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public Message sendMessage(String chatId, MessageBuilder messageBuilder) {
        return null;
    }

    @Override
    public Message sendMessage(String chatId, String text) {
        return null;
    }
}
