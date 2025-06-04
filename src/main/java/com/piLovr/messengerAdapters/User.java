package com.piLovr.messengerAdapters;

import lombok.Getter;

@Getter
public class User {
    private final String id;
    private final String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return "User{" + "id='" + id + ", name='" + name + '}';
    }
}
