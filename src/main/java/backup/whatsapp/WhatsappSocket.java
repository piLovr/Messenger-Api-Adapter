package backup.whatsapp;

import backup.Socket;
import backup.adapters.ExtendedMessage;
import backup.whatsappMobile.WhatsappMobileSocket;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.api.WhatsappWebHistoryPolicy;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;

public class WhatsappSocket extends Socket {
    public Whatsapp sock;

    public WhatsappSocket(String alias) {
        super(alias);
        if (!(this instanceof WhatsappMobileSocket)) {
            this.sock = Whatsapp.builder().webClient()
                    .newConnection(alias)
                    .historySetting(WhatsappWebHistoryPolicy.discard(false))
                    .unregistered(QrHandler.toTerminal());

            //.unregistered(phoneNumber, PairingCodeHandler.toTerminal())
        }
    }

    @Override
    public void connect() {
        sock.addListener(new WhatsappListener(this));
        sock.connect();
        this.connected = true;
        //sock.awaitDisconnection();
        new Thread(() -> sock.waitForDisconnection()).start();
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

    public ExtendedMessage sendMessage(Jid chatId, String text, MessageInfo quoted) {
        return new WhatsappExtendedMessage(this, sock.sendMessage(chatId, text, quoted));
    }

    public void deleteMessage(ChatMessageInfo chatMessageInfo, boolean forAll){
        sock.deleteMessage(chatMessageInfo, forAll);
    }
}
