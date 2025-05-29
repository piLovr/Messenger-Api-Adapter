package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.adapters.Socket;
import com.piLovr.messengerAdapters.telegram.TelegramSocket;

public class Test {
    public static void main(String[] args) {
        // Example usage of the Socket class
        //Socket s = new DiscordSocket(System.getenv("DISCORD_BOT_TOKEN"));
        //Socket s = new WhatsappSocket("test");
        Socket s = new TelegramSocket(System.getenv("TELEGRAM_BOT_TOKEN"));
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
