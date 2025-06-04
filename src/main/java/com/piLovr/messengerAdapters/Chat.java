package com.piLovr.messengerAdapters;

import lombok.Getter;

@Getter
public class Chat {
    private final String id;
    private final String name;
    private String description;

    public Chat(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chat{" + "id='" + id + ", name='" + name + ", description='" + description + '}';
    }
}
