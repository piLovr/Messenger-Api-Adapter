package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.discord.DiscordSocket;
import com.piLovr.messengerAdapters.whatsapp.WhatsappSocket;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.ChatMessageInfo;

import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        // Example usage of the Socket class
        //Socket s = new DiscordSocket(System.getenv("BOT_TOKEN"));
        Socket s = new WhatsappSocket("test");
        s.connect();

        System.out.println("Connected");
        //block thread until the socket is disconnected
        while(s.isConnected()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
