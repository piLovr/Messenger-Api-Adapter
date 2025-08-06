package com.piLovr.messengerAdapters.message;

import com.piLovr.messengerAdapters.event.Event;
import com.piLovr.messengerAdapters.messengerStructures.Account;

import java.util.List;

public class Message extends Event {
    private String text;
    private List<Account> mentions;
    private Message quoted;
    private List<Attachement> attachements;
}