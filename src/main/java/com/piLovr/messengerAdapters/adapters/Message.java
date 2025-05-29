package com.piLovr.messengerAdapters.adapters;

import com.piLovr.messengerAdapters.User;
import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.attachments.Attachment;
import com.piLovr.messengerAdapters.Platform;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public abstract class Message {
    protected String text;

    protected Chat chat;
    protected User user;
    protected String id;

    protected Message quotedMessage;
    protected List<String> mentions;
    protected List<Attachment> attachments;

    protected Set<String> who;
    protected String prefix;
    protected Set<String> flags;
    protected Message child;
    protected List<String> splitText;
    protected List<String> splitNormedText;

    protected void generateVars(){

    }

}
