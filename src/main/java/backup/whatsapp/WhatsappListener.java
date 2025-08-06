package backup.whatsapp;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.node.Node;

public class WhatsappListener implements it.auties.whatsapp.api.WhatsappListener {
    private final WhatsappSocket sock;
    public WhatsappListener(WhatsappSocket sock) {
        this.sock = sock;
    }
    @Override
    public void onNewMessage(Whatsapp whatsapp, MessageInfo incoming) {
        sock.fireOnMessage(new WhatsappExtendedMessage(sock, incoming));
    }
    @Override
    public void onNodeReceived(Whatsapp whatsapp, Node incoming) {
        System.out.println("Node recieved: " + incoming.toString());
    }
}
/**
 * Node recieved: Node[description=notification, attributes={from=120363027398298532@g.us, type=w:gp2, id=1248473609, participant=4917693272357@s.whatsapp.net, addressing_mode=pn, notify=Jan, t=1753982580}, content=[Node[description=promote, attributes={v_id=1753982580592389, prev_v_id=1753982562795167}, content=[Node[description=participant, attributes={jid=380689028831@s.whatsapp.net}]]]]]
 * it.auties.whatsapp.model.info.ChatMessageInfo@c610df4d
 * Message received in ListenerTest: {"origin":"bocki4_jbeta", "id":"3EB0A69A1C1192D29", "text":"", "chat":{"id":"120363027398298532@g.us", "name":"120363027398298532", "description":"null"}, "user":{"id":"4917693272357@s.whatsapp.net", "name":"null"}}
 * Node recieved: Node[description=iq, attributes={from=s.whatsapp.net, type=result, id=2e2ce59d092b, t=1753982585}]
 */