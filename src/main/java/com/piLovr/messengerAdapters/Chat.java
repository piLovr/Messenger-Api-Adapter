package com.piLovr.messengerAdapters;

import lombok.Getter;

@Getter
public class Chat {
    private String id;
    private String name;
    private String description;

    public Chat(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
