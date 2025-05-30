package com.piLovr.messengerAdapters.adapters;

import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.attachments.Attachment;
import lombok.Getter;

import java.util.List;
import java.util.Set;


public abstract class Message {
    @Getter protected String text;

    protected Chat chat;
    protected User user;
    @Getter protected String id;

    protected Message quotedMessage;
    protected List<String> mentions;
    protected List<Attachment> attachments;

    protected Set<String> who;
    @Getter protected String prefix;
    protected Set<Character> flags;
    protected Message child; //TODO
    @Getter protected List<String> splitText;
    @Getter protected List<String> splitLowerCase;

    protected void generateVars(){

    }

    public abstract Chat getChat();
    public abstract User getUser();
    public abstract Message getQuotedMessage();
    public abstract List<String> getMentions();
    public abstract List<Attachment> getAttachments();
    public abstract Set<String> getWho();
    public boolean containsFlag(Character flag){
        return flags.contains(flag);
    }; //TODO
    public String traverseWords(){
        return null; //TODO
    }
    //public abstract Message getChild();


    public abstract Message reply(Message message);
    public abstract Message reply(String text);
    public abstract Message replywQuote(String text);
}
