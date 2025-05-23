package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.Message;
import com.piLovr.messengerAdapters.adapters.MessageBuilder;

public interface UnifiedSocket {
    void connect();
    void disconnect();
    Message sendMessage(String chatId, MessageBuilder messageBuilder);
    Message sendMessage(String chatId, String text);
}
