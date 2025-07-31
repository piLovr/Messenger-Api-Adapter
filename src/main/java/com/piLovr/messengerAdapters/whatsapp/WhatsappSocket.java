package com.piLovr.messengerAdapters.whatsapp;

import com.piLovr.messengerAdapters.Socket;
import com.piLovr.messengerAdapters.adapters.ExtendedMessage;
import com.piLovr.messengerAdapters.whatsappMobile.WhatsappMobileSocket;
import it.auties.whatsapp.api.WebHistorySetting;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.standard.StickerMessageBuilder;

import java.util.UUID;

public class WhatsappSocket extends Socket {
    public Whatsapp sock;

    public WhatsappSocket(String alias) {
        super(alias);
        if (!(this instanceof WhatsappMobileSocket)) {
            this.sock = Whatsapp.webBuilder()
                    .newConnection(alias)
                    .historySetting(WebHistorySetting.discard(false))
                    .unregistered(QrHandler.toTerminal());

            //.unregistered(phoneNumber, PairingCodeHandler.toTerminal())
        }
    }

    @Override
    public void connect() {
        sock.addListener(new WhatsappListener(this));
        sock.connect().join();
        this.connected = true;
        //sock.awaitDisconnection();
        new Thread(() -> sock.awaitDisconnection()).start();
        System.out.println("Connected to Whatsapp");
    }

    @Override
    public void disconnect() {
        sock.disconnect();
    }

    @Override
    public ExtendedMessage sendMessage(String chatId, ExtendedMessage extendedMessage) {
        return null;
    }

    public ExtendedMessage sendMessage(Jid chatId, ExtendedMessage extendedMessageBuilder) {
        return null;
    }

    @Override
    public ExtendedMessage sendMessage(String chatId, String text) {
        return null; //sock.sendMessage(chatId, text);
    }

    public ExtendedMessage sendMessage(Jid chatId, String text) {
        return null; //new WhatsappExtendedMessage(this, sock.sendMessage(chatId, text).join());
    }

    public ExtendedMessage sendMessage(Jid chatId, String text, MessageInfo<?> quoted) {
        return new WhatsappExtendedMessage(this, sock.sendMessage(chatId, text, quoted).join());
    }
}
