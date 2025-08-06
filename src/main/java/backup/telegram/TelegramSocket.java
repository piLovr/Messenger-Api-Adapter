package backup.telegram;

import backup.Socket;
import backup.adapters.ExtendedMessage;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramSocket extends Socket {

    private TelegramBotsLongPollingApplication listenerClient;
    private TelegramClient sendClient;
    private String token;
    public TelegramSocket(String token) {
        super("telegram");
        listenerClient = new TelegramBotsLongPollingApplication();
        this.token = token;
    }
    public TelegramSocket(String token, String alias) {
        super(alias);
        listenerClient = new TelegramBotsLongPollingApplication();
        this.token = token;
    }
    @Override
    public void connect() {
        try {
            listenerClient.registerBot(token, () -> TelegramUrl.DEFAULT_URL, new CustomGetUpdatesGenerator(), new TelegramListener(this));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to start Telegram listener client", e);
        }
        sendClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public ExtendedMessage sendMessage(String chatId, ExtendedMessage extendedMessageBuilder) {
        return null;
    }

    @Override
    public ExtendedMessage sendMessage(String chatId, String text) {
        SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
        try{
            return new TelegramExtendedMessage(this, sendClient.execute(message));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }

    public ExtendedMessage sendMessage(String chatId, String text, int replyToMessageId) {
        SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(chatId)
                .text(text)
                .replyToMessageId(replyToMessageId)
                .build();
        try{
            return new TelegramExtendedMessage(this, sendClient.execute(message));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }
}
