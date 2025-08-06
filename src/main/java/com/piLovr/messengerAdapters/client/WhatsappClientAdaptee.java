package com.piLovr.messengerAdapters.client;

import com.piLovr.messengerAdapters.listener.Listener;
import com.piLovr.messengerAdapters.message.Message;

public class WhatsappClientAdaptee implements Client{
    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public Message sendMessage(String chatId, String text) {
        return null;
    }

    @Override
    public Message sendMessage(String chatId, Message message) {
        return null;
    }

    @Override
    public void addListener(Listener listener) {

    }

    @Override
    public void removeListener(Listener listener) {

    }

    @Override
    public String getAlias() {
        return "";
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
