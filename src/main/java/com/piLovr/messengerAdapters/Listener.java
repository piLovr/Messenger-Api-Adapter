package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.Message;

public class Listener {
    public void onMessageReceived(Message message) {
        // Handle the received message
        System.out.println("Message received: " + message);
    }
}
