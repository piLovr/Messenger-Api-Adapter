package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.ExtendedMessage;

public class ListenerTest implements Listener{
    @Override
    public void onMessage(ExtendedMessage extendedMessage) {
        System.out.println("Message received in ListenerTest: " + extendedMessage.toPrettyString());
    }
     
}
