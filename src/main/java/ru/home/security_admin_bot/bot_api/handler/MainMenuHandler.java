package ru.home.security_admin_bot.bot_api.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.InputMessageHandler;
import ru.home.security_admin_bot.service.MainMenuService;
import ru.home.security_admin_bot.service.ReplyMessageService;

@Component
public class MainMenuHandler implements InputMessageHandler {
    private final ReplyMessageService messagesService;
    private final MainMenuService mainMenuService;

    public MainMenuHandler(ReplyMessageService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(int userId, long chatId, String text) {
        return mainMenuService.getMainMenuMessage(chatId, messagesService.getReplyText("reply.showMainMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
