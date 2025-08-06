package backup;

import backup.adapters.ExtendedMessage;
import backup.adapters.Message;

@SuppressWarnings("unused")
public interface Listener {
    default void onMessage(ExtendedMessage extendedMessage) {

    }

    default void onReaction(Message message){

    }

    default void onMessageByMe(ExtendedMessage extendedMessage) {

    }

    default void onSlashCommand(Message message) {

    }

    default void onChatEvent(ChatEvent chatEvent){

    }
}
