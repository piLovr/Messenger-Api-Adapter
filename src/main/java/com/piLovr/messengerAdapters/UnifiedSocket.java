package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.ExtendedMessage;

public interface UnifiedSocket {
    void connect();
    void disconnect();
    ExtendedMessage sendMessage(String chatId, ExtendedMessage extendedMessageBuilder);
    ExtendedMessage sendMessage(String chatId, String text);
}
