package com.piLovr.messengerAdapters.client;

public interface Client {
    void connect();
    void disconnect();
    ExtendedMessage sendMessage(String chatId, String text);
    ExtendedMessage sendMessage(String chatId, ExtendedMessage message);

    void addListener(MessageListener listener);
    String getAlias();
    boolean isConnected();
}
