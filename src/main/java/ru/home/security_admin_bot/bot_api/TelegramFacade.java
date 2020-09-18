package ru.home.security_admin_bot.bot_api;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramFacade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TelegramFacade.class);

    public TelegramFacade() {
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        return null;
    }
}
