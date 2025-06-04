package com.piLovr.messengerAdapters.telegram;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramListener implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramSocket sock;
    public TelegramListener(TelegramSocket sock) {
        this.sock = sock;
    }
    @Override
    public void consume(List<Update> updates) {
        System.out.println(updates);
        //check if 2 updates have the same message.mediaGroupId
        //if so, merge them into one message with multiple attachments
        Map<String, List<Update>> mediaGroupMap = new HashMap<>();
        for(Update update : updates) {
            if(update.hasMessage()){
                if (update.getMessage().getMediaGroupId() != null) {
                    if(!mediaGroupMap.containsKey(update.getMessage().getMediaGroupId())) {
                        mediaGroupMap.put(update.getMessage().getMediaGroupId(), new java.util.ArrayList<>());
                    }else{
                        mediaGroupMap.get(update.getMessage().getMediaGroupId()).add(update);
                    }
                }else{
                    // Handle single message updates
                    TelegramExtendedMessage m = new TelegramExtendedMessage(sock, update);
                    sock.fireOnMessage(m);
                }
            }else if(update.getMessageReaction() != null){
                //sock.fireOnReaction();
            }

        }
        for(Map.Entry<String, List<Update>> entry : mediaGroupMap.entrySet()) {
            List<Update> mediaGroupUpdates = entry.getValue();

            // Create a TelegramMessage with the media group updates
            sock.fireOnMessage(new TelegramExtendedMessage(sock, mediaGroupUpdates));
        }
        //LongPollingSingleThreadUpdateConsumer.super.consume(updates);
    }

    @Override
    public void consume(Update update) {
        if(update.hasMessage()) {
            //sock.fireOnMessage(new TelegramMessage(sock, update));
        } else {
            System.out.println("Received update without message: " + update);
        }
    }
}
