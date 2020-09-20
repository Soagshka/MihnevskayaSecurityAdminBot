package ru.home.security_admin_bot.bot_api.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.InputMessageHandler;
import ru.home.security_admin_bot.service.MainMenuService;
import ru.home.security_admin_bot.service.ReplyMessageService;

@Component
public class HelpMenuHandler implements InputMessageHandler {
    private final MainMenuService mainMenuService;
    private final ReplyMessageService messagesService;

    public HelpMenuHandler(MainMenuService mainMenuService, ReplyMessageService messagesService) {
        this.mainMenuService = mainMenuService;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(int userId, long chatId, String text) {
        return mainMenuService.getMainMenuMessage(chatId,
                messagesService.getReplyText("reply.showHelpMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP;
    }
}
