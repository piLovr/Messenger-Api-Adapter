package com.piLovr.messengerAdapters.whatsapp;

public class UnsupportedMessageType extends RuntimeException {
    public UnsupportedMessageType(String message) {
        super(message);
    }
}
