package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import com.piLovr.messengerAdapters.adapters.Message;

@SuppressWarnings("unused")
public interface Listener {
    default void onMessage(ExtendedMessage extendedMessage) {

    }

    default void onReaction(Message message){

    }

    default void onMessageByMe(ExtendedMessage extendedMessage) {

    }

    default void onSlashCommand(Message message) {

    }
}
