package com.piLovr.messengerAdapters.telegram;

import com.piLovr.messengerAdapters.Listener;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramListener implements LongPollingSingleThreadUpdateConsumer {
    @Override
    public void consume(List<Update> updates) {
        System.out.println(updates);

        //check if 2 updates have the same message.mediaGroupId
        //if so, merge them into one message with multiple attachments
        Map<String, List<Update>> mediaGroupMap = new HashMap<>();
        for(Update update : updates) {
            if(!update.hasMessage()) return;
            if (update.getMessage().getMediaGroupId() != null) {
                if(!mediaGroupMap.containsKey(update.getMessage().getMediaGroupId())) {
                    mediaGroupMap.put(update.getMessage().getMediaGroupId(), new java.util.ArrayList<>());
                }else{
                    mediaGroupMap.get(update.getMessage().getMediaGroupId()).add(update);
                }
            }else{
                // Handle single message updates
                Listener.onMessage(new TelegramMessage(update));
            }
        }
        for(Map.Entry<String, List<Update>> entry : mediaGroupMap.entrySet()) {
            List<Update> mediaGroupUpdates = entry.getValue();

            // Create a TelegramMessage with the media group updates
            Listener.onMessage(new TelegramMessage(mediaGroupUpdates));
        }
        //LongPollingSingleThreadUpdateConsumer.super.consume(updates);
    }

    @Override
    public void consume(Update update) {}
}
