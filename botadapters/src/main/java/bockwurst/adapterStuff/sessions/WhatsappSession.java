package bockwurst.adapterStuff.sessions;

import bockwurst.adapterStuff.exceptions.WhatsappSessionException;
import bockwurst.adapterStuff.listeners.WhatsappListener;
import it.auties.whatsapp.api.*;
import it.auties.whatsapp.model.chat.Chat;
import it.auties.whatsapp.model.mobile.VerificationCodeMethod;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import bockwurst.adapterStuff.models.Jid;

public class WhatsappSession extends Session{
    private Long phoneNumber;

    private VerificationCodeMethod mobileVerificationMethod;
    private boolean pairingCodeRequested = false;
    private Whatsapp whatsapp;
    private boolean isActive;

    private Jid jid;
    private DisconnectReason disconnectReason;

    //private static List<Jid> groups = new ArrayList<>();

    private String qrCode;
    private String pairingCode;

    public void start(){
        if(mobileVerificationMethod == null){
            WebOptionsBuilder rawConnection = Whatsapp.webBuilder()
                    .newConnection(this.alias)
                    .historyLength(WebHistoryLength.extended());

            // Create a stream to hold the output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream temp = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(temp);

            if(pairingCodeRequested){
                if(phoneNumber == null){
                    throw new WhatsappSessionException("No phone number provided");
                }
                //pairing code
                whatsapp = rawConnection.unregistered(phoneNumber, PairingCodeHandler.toTerminal());
                pairingCode = baos.toString();
                //sendPairingCode(baos.toString());
            }else{
                //qr code
                whatsapp = rawConnection.unregistered(QrHandler.toPlainString(System.out::println));
                qrCode = baos.toString();
                //sendQrCode(baos.toString());
            }


            System.out.flush();
            System.setOut(old);
        }else{
            if(phoneNumber == null){
                throw new WhatsappSessionException("No phone number provided");
            }
            //mobile
            whatsapp = Whatsapp.mobileBuilder()
                    .newConnection(this.alias)
                    .unregistered()
                    .verificationCodeMethod(mobileVerificationMethod)
                    .verificationCodeSupplier(this::getCode)
                    .register(phoneNumber)
                    .join()
                    .whatsapp();
        }

        whatsapp.addListener(new WhatsappListener(this, whatsapp));
        whatsapp.connect().join().awaitDisconnection();
    }

    public WhatsappSession(){

    }
    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMobileVerificationMethod(VerificationCodeMethod mobileVerificationMethod) {
        this.mobileVerificationMethod = mobileVerificationMethod;
    }

    public void setPairingCodeRequested(boolean pairingCodeRequested) {
        this.pairingCodeRequested = pairingCodeRequested;
    }


    private CompletableFuture<String> getCode() {
        CompletableFuture<String> future = new CompletableFuture<>();
        /*
        if(isRemoteStart){
            ChatMessageInfo message = (ChatMessageInfo) TextMessage.of("Code sent. Reply to this message with the Code.").quote(messageData.messageContainer).whatsapp(remoteWhatsapp,messageData.from);
            MessageData reply = new MessageData(remoteWhatsapp.awaitMessageReply(message).join());
            reply.generateWhatsapp();
            future.complete(reply.text);
        }else{
        */
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter verification code: ");
            future.complete(scanner.nextLine());
        //}
        return future;
    }


    /*
    public void sendQrCode(String plainQr) {
        try{
            ImageMessage.of(QrCodeGenerator.generateImage(plainQr, 250, 250), "Qr code for " + name).whatsapp(remoteWhatsapp, messageData.from);
        } catch (WriterException | IOException e) {
            throw new SessionConnectExcception(e);
        }
    }
    public void sendPairingCode(String pairingCode){
        remoteWhatsapp.sendMessage(messageData.from.getJid(), "Here is your pairingCode: " + pairingCode);
    }
    */
    public boolean isFirstConnection() {
        return isFirstConnection;
    }

    public Jid getJid(){
        return jid;
    }
    public String getName() {
        return name;
    }
    public void setDisconnectReason(DisconnectReason disconnectReason) {
        this.disconnectReason = disconnectReason;
    }
    public DisconnectReason getDisconnectReason() {
        return disconnectReason;
    }
    public void reloadGroups(){
        groups = whatsapp.store().chats().stream().filter(Chat::isGroup).map(Chat::jid).map(Jid::new).toList();
        Database.updateSessionGroups(name, groups);
    }
    public void groupsOnLoggedIn(Collection<Chat> chats){
        groups = chats.stream().filter(Chat::isGroup).map(Chat::jid).map(Jid::new).toList();
        Database.updateSessionGroups(name, groups);
    }
    public boolean isParticipating(Jid group){
        return groups.contains(group);
    }
    public List<Jid> getGroups(){
        return groups;
    }
    public void setActive(boolean active){
        isActive = active;
    }
    public boolean isActive(){
        return isActive;
    }
    public int getPriority(){
        return priority;
    }
    public boolean isMobile(){
        return isMobile;
    }
    public long getPhoneNumber(){
        return phoneNumber;
    }
}
