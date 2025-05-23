package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.discord.DiscordSocket;
import com.piLovr.messengerAdapters.whatsapp.WhatsappSocket;

public class Test {
    public static void main(String[] args) {
        // Example usage of the Socket class
        //Socket s = new DiscordSocket(System.getenv("BOT_TOKEN"));
        Socket s = new WhatsappSocket("test");
        s.connect();
    }
}
