package backup.telegram;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@NoArgsConstructor
public class CustomGetUpdatesGenerator implements Function<Integer, GetUpdates> {
    private static final int GET_UPDATES_LIMIT = 100;
    private static final int GET_UPDATES_TIMEOUT = 50;

    private List<String> allowedUpdates = new ArrayList<>();

    @Override
    public GetUpdates apply(Integer lastReceivedUpdate) {

        allowedUpdates.add("message");
        allowedUpdates.add("edited_message");
        allowedUpdates.add("message_reaction");
        allowedUpdates.add("poll");
        allowedUpdates.add("poll_answer");
        allowedUpdates.add("chat_member");
        allowedUpdates.add("chat_join_request");

        /*
        allowedUpdates.add("channel_post");
        allowedUpdates.add("edited_channel_post");
        allowedUpdates.add("business_connection");
        allowedUpdates.add("business_message");
        allowedUpdates.add("edited_business_message");
        allowedUpdates.add("deleted_business_messages");

        allowedUpdates.add("message_reaction_count");
        allowedUpdates.add("inline_query");
        allowedUpdates.add("chosen_inline_result");
        allowedUpdates.add("callback_query");
        allowedUpdates.add("shipping_query");
        allowedUpdates.add("pre_checkout_query");
        allowedUpdates.add("purchased_paid_media");

        allowedUpdates.add("my_chat_member");

        allowedUpdates.add("chat_boost");
        allowedUpdates.add("removed_chat_boost");
        */
        // You can add more allowed updates as needed

        return GetUpdates
                .builder()
                .limit(GET_UPDATES_LIMIT)
                .timeout(GET_UPDATES_TIMEOUT)
                .offset(lastReceivedUpdate + 1)
                .allowedUpdates(allowedUpdates)
                .build();
    }
}
/*
update_id
message
edited_message
channel_post
edited_channel_post
business_connection
business_message
edited_business_message
deleted_business_messages
message_reaction
message_reaction_count
inline_query
chosen_inline_result
callback_query
shipping_query
pre_checkout_query
purchased_paid_media
poll
poll_answer
my_chat_member
chat_member
chat_join_request
chat_boost
removed_chat_boost
 */