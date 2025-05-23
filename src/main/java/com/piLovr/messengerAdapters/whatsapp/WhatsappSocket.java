package com.piLovr.messengerAdapters.whatsapp;

import com.piLovr.messengerAdapters.Socket;
import com.piLovr.messengerAdapters.adapters.Message;
import com.piLovr.messengerAdapters.adapters.MessageBuilder;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebHistorySetting;
import it.auties.whatsapp.api.Whatsapp;

public class WhatsappSocket extends Socket {
    protected Whatsapp sock;
    public WhatsappSocket(String alias) {
         this.sock = Whatsapp.webBuilder()
                .newConnection(alias)
                .historySetting(WebHistorySetting.discard(false))
                .unregistered(QrHandler.toTerminal());
        //.unregistered(phoneNumber, PairingCodeHandler.toTerminal())
    }

    public WhatsappSocket(){

    }
    @Override
    public void connect() {
        sock.addListener(new WhatsappListener());
        sock.connect().join();
        new Thread(() -> sock.awaitDisconnection()).start();
    }

    @Override
    public void disconnect() {
        sock.disconnect();
    }

    @Override
    public Message sendMessage(String chatId, MessageBuilder messageBuilder) {
        return null;
    }

    @Override
    public Message sendMessage(String chatId, String text) {
        return null; //sock.sendMessage(chatId, text);
    }
}
