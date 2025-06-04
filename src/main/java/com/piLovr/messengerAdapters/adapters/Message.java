package com.piLovr.messengerAdapters.adapters;

import com.piLovr.messengerAdapters.Chat;
import com.piLovr.messengerAdapters.User;
import lombok.Getter;

import java.util.List;

public abstract class Message {
    @Getter protected String origin;
    @Getter protected String text;

    @Getter protected Chat chat;
    @Getter protected User user;
    @Getter protected String id;

    @Getter
    protected List<String> splitText;
    @Getter protected List<String> splitLowerCase;
    private int wordCount = 0;

    public String traverseWords(){
        if(splitLowerCase == null || splitLowerCase.isEmpty()){
            //convert Text to lower case and split by spaces
            splitLowerCase = List.of(text.toLowerCase().split("\\s+"));
            splitText = List.of(text.split("\\s+"));
        }
        if(wordCount >= splitLowerCase.size()){
            return null;
        }
        return splitLowerCase.get(wordCount++);
    }
    //public abstract Message getChild();

    @Override
    public String toString() {
        return String.format("Message{origin='%s', id='%s', text='%s', chat=%s, user=%s}", origin, id, text, chat, user);
    }

    public String toJson(){
        return String.format("{\"origin\":\"%s\", \"id\":\"%s\", \"text\":\"%s\", \"chat\":%s, \"user\":%s}",
                origin, id, text, chat.toJson(), user.toJson());
    }
}
