package com.piLovr.messengerAdapters.client;


import com.piLovr.messengerAdapters.listener.Listener;
import com.piLovr.messengerAdapters.message.Message;

public interface Client {
    void connect();
    void disconnect();
    Message sendMessage(String chatId, String text);
    Message sendMessage(String chatId, Message message);

    void addListener(Listener listener);
    void removeListener(Listener listener);

    String getAlias();
    boolean isConnected();
}
