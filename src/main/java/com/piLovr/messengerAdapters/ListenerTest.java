package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.ExtendedMessage;

public class ListenerTest implements Listener{
    @Override
    public void onMessage(ExtendedMessage extendedMessage) {
        System.out.println("Message received in ListenerTest: " + extendedMessage.toJson());
        if(extendedMessage.getText().equalsIgnoreCase("ping")) {
            extendedMessage.reply("pong");
            extendedMessage.replyWithQuote("pong2");
        }
    }
    @Override
    public void onReaction(com.piLovr.messengerAdapters.adapters.Message message) {
        System.out.println("Reaction received in ListenerTest: " + message.toString());
    }
     
}
