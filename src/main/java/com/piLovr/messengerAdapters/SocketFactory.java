package com.piLovr.messengerAdapters;

import com.piLovr.messengerAdapters.discord.DiscordSocket;
import com.piLovr.messengerAdapters.telegram.TelegramSocket;
import com.piLovr.messengerAdapters.whatsapp.WhatsappSocket;
import com.piLovr.messengerAdapters.whatsappMobile.WhatsappMobileSocket;

public class SocketFactory {
    /**
     * Creates a new Socket instance based on the provided type.
     *
     * @param type The type of socket to create.
     * @return A new Socket instance.
     */
    public static Socket createSocketByEnv(String type, String alias) {
        return switch (type.toLowerCase()) {
            case "discord" ->
                    new DiscordSocket(System.getenv("DISCORD_BOT_TOKEN"), alias);
            case "whatsapp", "whatsapp-web", "whatsapp_web" ->
                    new WhatsappSocket(alias);
            case "whatsapp-mobile", "whatsapp_mobile" ->
                    new WhatsappMobileSocket(alias);
            case "telegram" ->
                    new TelegramSocket(System.getenv("TELEGRAM_BOT_TOKEN"), alias);
            default -> throw new IllegalArgumentException("Unsupported socket type: " + type);
        };
    }
    public static Socket createSocketByEnv(Platform platform, String alias) {
        return createSocketByEnv(platform.name().toLowerCase(), alias);
    }
}
