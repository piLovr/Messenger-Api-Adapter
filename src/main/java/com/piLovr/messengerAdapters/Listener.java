package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.Message;

public interface Listener {
    static void onMessage(Message message){
        System.out.println("Message received");
    }
}
