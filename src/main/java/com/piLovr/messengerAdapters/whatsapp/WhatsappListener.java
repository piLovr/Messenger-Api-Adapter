package com.piLovr.messengerAdapters.whatsapp;

import it.auties.whatsapp.api.Listener;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.node.Node;

public class WhatsappListener implements Listener {
    @Override
    public void onNewMessage(Whatsapp whatsapp, MessageInfo<?> incoming) {
        com.piLovr.messengerAdapters.Listener.onMessage(new WhatsappMessage(incoming));
    }
    @Override
    public void onNodeReceived(Whatsapp whatsapp, Node incoming) {
        System.out.println("Node recieved: " + incoming.toJson());
    }
}
