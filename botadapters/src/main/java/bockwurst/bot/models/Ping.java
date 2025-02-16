package bockwurst.bot.models;

import it.auties.whatsapp.model.info.MessageInfo;

import java.sql.Timestamp;

public record Ping(MessageInfo<?> originalMessage, String newMessageId, Timestamp timestamp) {}
