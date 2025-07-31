package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.discord.DiscordSocket;
import com.piLovr.messengerAdapters.telegram.TelegramSocket;
import com.piLovr.messengerAdapters.whatsapp.WhatsappSocket;

public class Test {
    public static void main(String[] args) {
        // Example usage of the Socket class
        //Socket s3 = new DiscordSocket(System.getenv("DISCORD_BOT_TOKEN"));
        Socket s = new WhatsappSocket("bocki4_jbeta");
        //Socket s= new TelegramSocket(System.getenv("TELEGRAM_BOT_TOKEN"));
        s.addListener(new ListenerTest());
        //s2.addListener(new ListenerTest());
        //s3.addListener(new ListenerTest());
        s.connect();
        //s2.connect();
        //s3.connect();

        System.out.println("Connected");
        //block thread until the socket is disconnected
        /*
        while(s.isConnected()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/


    }
}
