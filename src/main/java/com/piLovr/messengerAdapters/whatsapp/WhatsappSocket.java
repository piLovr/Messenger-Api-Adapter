package com.piLovr.messengerAdapters.whatsapp;

import com.piLovr.messengerAdapters.adapters.Socket;
import com.piLovr.messengerAdapters.adapters.Message;
import com.piLovr.messengerAdapters.adapters.MessageBuilder;
import it.auties.whatsapp.api.WebHistorySetting;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;

import java.util.UUID;

public class WhatsappSocket extends Socket {
    protected Whatsapp sock;
    public WhatsappSocket(String alias) {
        UUID uuid = UUID.fromString("8ffb15c9-c20a-4a8f-9483-7920a530cbd2");
        System.out.println(uuid);
        this.sock = Whatsapp.webBuilder()
                .newConnection(uuid)
                .historySetting(WebHistorySetting.discard(false))
                .unregistered(QrHandler.toTerminal());

        //.unregistered(phoneNumber, PairingCodeHandler.toTerminal())
    }

    public WhatsappSocket(){

    }
    @Override
    public void connect() {
        sock.addListener(new WhatsappListener(this));
        sock.connect().join();
        this.connected = true;
        sock.awaitDisconnection();
        //new Thread(() -> sock.awaitDisconnection()).start();
        System.out.println("Connected to Whatsapp");
    }

    @Override
    public void disconnect() {
        sock.disconnect();
    }

    @Override
    public Message sendMessage(String chatId, Message messageBuilder) {
        return null;
    }

    public Message sendMessage(Jid chatId, Message messageBuilder) {
        return null;
    }

    @Override
    public Message sendMessage(String chatId, String text) {
        return null; //sock.sendMessage(chatId, text);
    }

    public Message sendMessage(Jid chatId, String text) {
        return new WhatsappMessage(this, sock.sendMessage(chatId, text).join());
    }

    public Message sendMessage(Jid chatId, String text, MessageInfo<?> quoted) {
        return new WhatsappMessage(this, sock.sendMessage(chatId, text, quoted).join());
    }
}
