package bockwurst.adapterStuff.listeners;

import bockwurst.adapterStuff.sessions.WhatsappSession;
import bockwurst.bot.models.Ping;
import it.auties.whatsapp.api.DisconnectReason;
import it.auties.whatsapp.api.SocketEvent;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.controller.Store;
import it.auties.whatsapp.listener.Listener;
import it.auties.whatsapp.model.action.Action;
import it.auties.whatsapp.model.call.Call;
import it.auties.whatsapp.model.chat.Chat;
import it.auties.whatsapp.model.contact.Contact;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageIndexInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.info.QuotedMessageInfo;
import it.auties.whatsapp.model.jid.JidProvider;
import it.auties.whatsapp.model.mobile.CountryLocale;
import it.auties.whatsapp.model.newsletter.Newsletter;
import it.auties.whatsapp.model.node.Node;
import it.auties.whatsapp.model.privacy.PrivacySettingEntry;
import it.auties.whatsapp.model.setting.Setting;

import java.util.*;

public class WhatsappListener implements Listener {
    private final WhatsappSession whatsappSession;
    private final Whatsapp whatsapp;

    //list of ping commands that have been initiated
    private static final HashSet<Ping> pendingPings = new HashSet<>();

    public WhatsappListener(WhatsappSession whatsappSession, Whatsapp whatsapp){
        this.whatsappSession = whatsappSession;
        this.whatsapp = whatsapp;
    }

    /**
     * Called when a new message is received in a chat
     *
     * @param info the message that was sent
     */
    @Override
    public void onNewMessage(MessageInfo<?> info) {
        if(info.message().isEmpty()){
            System.out.println("Empty message: " + info.toJson());
            return;
        }
        System.out.println("Groups loaded? " + !whatsappSession.getGroups().isEmpty()); //TODO add listeners ONLY as soon as groups are loaded (+test that lol)
        while(whatsappSession.getGroups().isEmpty()){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        MessageData d = new MessageData(info);

        d.user = Users.getUserOrCreateNew(d.sender, d.pushName);
        boolean isPing2 = false;
        for(Ping ping : pendingPings){
            if(ping.originalMessage().id().equals(info.id())){
                isPing2 = true;
                pendingPings.remove(ping);
                break;
            }
        }
        if(d.user.isBanned() || (!isPing2) && TimeManager.isInSpamFilter(d.user.getId()))) return;

        if(d.isGroup){
            d.group = Groups.getGroupOrCreateNew(d.from, whatsappSession);
            d.group.initMetadataOrDont(whatsapp);
            if(!WhatsappSessions.isMyTurn(whatsappSession, d.group)) return;
            d.user.addBaseGroupRight(d.group.getGroupRole(d.user));

            CounterGame.check(d, whatsapp);
        }

        d.generateWhatsapp();
        System.out.println(d);

        if(d.text != null){
            CommandResult res = commandHandler.runCommand(whatsapp, d);
            if(res != null) System.out.println("Sent message: " + res.whatsapp(whatsapp, d.from).toJson());
            //TODO Countergame
        }
        if (StickerManager.checkAutoSticker(d)) {
            //todo sticker krams
        }
    }

    /**
     * Called when the socket receives all the chats from WhatsappWeb's Socket. When this event is
     * fired, it is guaranteed that all metadata excluding messages will be present. To access this
     * data use {@link Store#chats()}. If you also need the messages to be loaded, please refer to
     * {@link Listener#onChatMessagesSync(Chat, boolean)}. Particularly old chats may come later
     * through {@link Listener#onChatMessagesSync(Chat, boolean)}.
     *
     * @param chats the chats
     */
    @Override
    public void onChats(Collection<Chat> chats){
        whatsappSession.groupsOnLoggedIn(chats);
        System.out.println("Chats loaded:");
        for(Chat chat : chats){
            if(chat.isGroup()) {
                System.out.println("    " + chat.name() + " | " + chat.jid());
            }
        }
    }

    /**
     * Called when a message is deleted
     *
     * @param info     the message that was deleted
     * @param everyone whether this message was deleted by you only for yourself or whether the
     *                 message was permanently removed
     */
    @Override
    public void onMessageDeleted(MessageInfo<?> info, boolean everyone) {
        //todo hehe
    }


    /**
     * Called when the socket successfully establishes a connection and logs in into an account. When
     * this event is called, any data, including chats and contact, is not guaranteed to be already in
     * memory. Instead, {@link Listener#onChats(Collection)} and
     * {@link Listener#onContacts(Collection)} should be used.
     */
    @Override
    public void onLoggedIn(){
        WhatsappSessions.sessionUp(whatsappSession);
        System.out.println("Logged in");
        //whatsapp.sendMessage((new Jid("120363297041378280@g.us")).getJid(), "Hello World");
    }

    /**
     * Called when the socket successfully disconnects from WhatsappWeb's Socket
     *
     * @param reason the errorReason why the session was disconnected
     */
    @Override
    public void onDisconnected(DisconnectReason reason){
        WhatsappSessions.sessionDown(whatsappSession, reason);
    }

    public static void addPendingPing(Ping ping){
        pendingPings.add(ping);

        //make sure to remove from list after 60 seconds
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    pendingPings.remove(ping);
                }
            },
            60000
        );
    }


    /**
     * Called when the socket receives a node from Whatsapp
     *
     * @param node the non-null node that was just received
     */
    public void onNodeReceived(Node node) {
        //System.out.println("Node received: " + node.toJson());
        if(node.hasDescription("receipt")){
            for (Ping ping : pendingPings) {
                if (ping.newMessageId().equals(node.attributes().getString("id"))) {
                    System.out.println("Got receipt for own reply, running again...");
                    onNewMessage(ping.originalMessage());
                    return;
                }
            }
        }
        /*NOGO
        Node received: {
          "description" : "ack",
          "attributes" : {
            "t" : "1728416445",
            "phash" : "2:rKQREey7",
            "count" : "2",
            "from" : "120363297041378280@g.us",
            "id" : "3EB0F4D274A5D87F127FD6",
            "class" : "message"
          }
        }

        GO
        Node received: {
          "description" : "receipt",
          "attributes" : {
            "t" : "1728416445",
            "from" : "120363297041378280@g.us",
            "id" : "3EB0F4D274A5D87F127FD6",
            "type" : "inactive",
            "participant" : "4915775230514@s.whatsapp.net"
          }
        }

        System.out.println("Node received: " + node.toJson());
        if(node.hasDescription("notification") && node.hasContent()){
            for(Node child : node.children()) {
                String nodeType = child.description();
                Jid from = new Jid(node.attributes().getString("from"));
                Jid actor = new Jid(node.attributes().getString("participant"));

                List<Jid> targets = new java.util.ArrayList<>();
                for(Node content : child.children()){
                    if(content.hasDescription("participant")){
                        targets.add(new Jid(content.attributes().getString("jid")));
                    }
                }
                Groups.listenerAction(whatsapp, nodeType, from, actor, targets);
            }
        }*/
    }


    /**
     * Called when the socket sends a node to Whatsapp
     *
     * @param outgoing the non-null node that was just sent
     */
    public void onNodeSent(Node outgoing) {
    }

    /**
     * Called when an updated list of properties is received. This method is called both when a
     * connection is established with WhatsappWeb and when new props are available. In the latter case
     * though, this object should be considered as partial and is guaranteed to contain only updated
     * entries.
     *
     * @param metadata the updated list of properties
     */
    public void onMetadata(Map<String, String> metadata) {
    }

    /**
     * Called when the socket receives a sync from Whatsapp.
     *
     * @param action           the sync that was executed
     * @param messageIndexInfo the data about this action
     */
    public void onAction(Action action, MessageIndexInfo messageIndexInfo) {
    }

    /**
     * Called when the socket receives a setting change from Whatsapp.
     *
     * @param setting the setting that was toggled
     */
    public void onSetting(Setting setting) {
    }

    /**
     * Called when the socket receives new features from Whatsapp.
     *
     * @param features the non-null features that were sent
     */
    public void onFeatures(List<String> features) {
    }

    /**
     * Called when the socket receives all the contacts from WhatsappWeb's Socket
     *
     * @param contacts the contacts
     */
    public void onContacts(Collection<Contact> contacts) {
    }

    /**
     * Called when the socket receives an update regarding the presence of a contact
     *
     * @param chat   the chat that this update regards
     * @param jid    the contact that this update regards
     */
    public void onContactPresence(Chat chat, JidProvider jid) {
    }

    /**
     * Called when the socket receives all the newsletters from WhatsappWeb's Socket
     *
     * @param newsletters the newsletters
     */
    public void onNewsletters(Collection<Newsletter> newsletters) {
    }

    /**
     * Called when the socket receives the message for a chat This method is only called when the QR
     * is first scanned and history is being synced. From all subsequent runs, the messages will
     * already in the chat on startup.
     *
     * @param chat the chat
     * @param last whether the messages in this chat are complete or there are more coming
     */
    public void onChatMessagesSync(Chat chat, boolean last) {
    }

    /**
     * Called when the socket receives the sync percentage for the full or recent chunk of messages.
     * This method is only called when the QR is first scanned and history is being synced.
     *
     * @param percentage the percentage synced up to now
     * @param recent     whether the sync is about the recent messages or older messages
     */
    public void onHistorySyncProgress(int percentage, boolean recent) {
    }

    /**
     * Called when the status of a message changes inside a chat
     *
     * @param info the message whose status changed
     */
    public void onMessageStatus(MessageInfo<?> info) { //todo für penis maybe
    }

    /**
     * Called when the socket receives all the status updated from WhatsappWeb's Socket.
     *
     * @param status the status
     */
    public void onStatus(Collection<ChatMessageInfo> status) {
    }

    /**
     * Called when the socket receives a new status from WhatsappWeb's Socket
     *
     * @param status the new status message
     */
    public void onNewStatus(ChatMessageInfo status) {
    }

    /**
     * Called when an event regarding the underlying is fired
     *
     * @param event the event
     */
    public void onSocketEvent(SocketEvent event) {
    }

    /**
     * Called when a message answers a previous message
     *
     * @param response the response
     * @param quoted   the quoted message
     */
    public void onMessageReply(ChatMessageInfo response, QuotedMessageInfo quoted) {
    }

    /**
     * Called when a contact's profile picture changes
     *
     * @param contact the contact whose pic changed
     */
    public void onProfilePictureChanged(Contact contact) {
    }

    /**
     * Called when a group's picture changes
     *
     * @param group the group whose pic changed
     */
    public void onGroupPictureChanged(Chat group) {
    }

    /**
     * Called when the companion's name changes
     *
     * @param oldName the non-null old name
     * @param newName the non-null new name
     */
    public void onNameChanged(String oldName, String newName) {
    }

    /**
     * Called when the companion's about changes
     *
     * @param oldAbout the non-null old about
     * @param newAbout the non-null new about
     */
    public void onAboutChanged(String oldAbout, String newAbout) {
    }

    /**
     * Called when the companion's locale changes
     *
     * @param oldLocale the non-null old locale
     * @param newLocale the non-null new picture
     */
    public void onLocaleChanged(CountryLocale oldLocale, CountryLocale newLocale) {
    }

    /**
     * Called when a contact is blocked or unblocked
     *
     * @param contact the non-null contact
     */
    public void onContactBlocked(Contact contact) {
    }

    /**
     * Called when the socket receives a new contact
     *
     * @param contact the new contact
     */
    public void onNewContact(Contact contact) {
    }

    /**
     * Called when a privacy setting is modified
     *
     * @param oldPrivacyEntry the old entry
     * @param newPrivacyEntry the new entry
     */
    public void onPrivacySettingChanged(PrivacySettingEntry oldPrivacyEntry, PrivacySettingEntry newPrivacyEntry) {
        //todo hoster krams
    }

    /**
     * Called when the list of companion devices is updated
     *
     * @param devices the non-null devices
     */
    public void onLinkedDevices(Collection<it.auties.whatsapp.model.jid.Jid> devices) {
        //todo hoster krams
    }

    /**
     * Called when an OTP is requested from a new device
     * Only works on the mobile API
     *
     * @param code the registration code
     */
    public void onRegistrationCode(long code) {
        //todo wichtiges ding
    }

    /**
     * Called when a phone call arrives
     *
     * @param call the non-null phone call
     */
    public void onCall(Call call) {

    }
}
