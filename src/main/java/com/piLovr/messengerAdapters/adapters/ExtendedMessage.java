package com.piLovr.messengerAdapters.adapters;

import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.attachments.Attachment;
import lombok.Getter;

import java.util.List;
import java.util.Set;


public abstract class ExtendedMessage extends Message {

    protected ExtendedMessage quotedMessage;
    protected List<String> mentions;
    protected List<Attachment> attachments;

    protected Set<String> who;
    @Getter protected String prefix;
    protected Set<Character> flags;
    protected ExtendedMessage child; //TODO


    protected void generateVars(){

    }

    public abstract ExtendedMessage getQuotedMessage();
    public abstract List<String> getMentions();
    public abstract List<Attachment> getAttachments();
    public Set<String> getWho(){
        if(who == null){
            who = Set.of();
        }
        return who;
    };
    public boolean containsFlag(Character flag){
        return flags.contains(flag);
    }; //TODO



    public abstract ExtendedMessage reply(ExtendedMessage extendedMessage);
    public abstract ExtendedMessage reply(String text);
    public abstract ExtendedMessage replyWithQuote(String text);

    public String toPrettyString() {
        return String.format("Message{id='%s', text='%s', chat=%s, user=%s}", id, text, chat, user);
    }
}
