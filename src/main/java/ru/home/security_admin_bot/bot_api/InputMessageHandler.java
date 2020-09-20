package ru.home.security_admin_bot.bot_api;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface InputMessageHandler {
    SendMessage handle(int userId, long chatId, String text);

    BotState getHandlerName();
}
