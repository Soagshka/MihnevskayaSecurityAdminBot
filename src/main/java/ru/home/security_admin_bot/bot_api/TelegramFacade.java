package ru.home.security_admin_bot.bot_api;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.security_admin_bot.controller.to.RecordData;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;
import ru.home.security_admin_bot.service.MainMenuService;
import ru.home.security_admin_bot.service.MultipleMessageSender;
import ru.home.security_admin_bot.util.BotStateUtil;

@Component
public class TelegramFacade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TelegramFacade.class);
    private final BotStateContext botStateContext;
    private final MainMenuService mainMenuService;
    private final MultipleMessageSender multipleMessageSender;
    private final UserEntityRepository userEntityRepository;

    public TelegramFacade(BotStateContext botStateContext, MainMenuService mainMenuService, MultipleMessageSender multipleMessageSender, UserEntityRepository userEntityRepository) {
        this.botStateContext = botStateContext;
        this.mainMenuService = mainMenuService;
        this.multipleMessageSender = multipleMessageSender;
        this.userEntityRepository = userEntityRepository;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        BotState botState;

        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.SHOW_MAIN_MENU;
                break;
            case "Заполнить данные для входа":
                botState = BotState.FILL_LOGIN;
                break;
            case "Последние 5 записей":
                botState = BotState.SHOW_5_LAST_RECORDS;
                break;
            case "Поиск заявки по номеру автомобиля":
                botState = BotState.SEARCH_BY_AUTO_NUMBER;
                break;
            case "Поиск заявки по номеру телефона":
                botState = BotState.SEARCH_BY_PHONE_NUMBER;
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP;
                break;
            default:
                botState = BotStateUtil.getBotState(userId, chatId);
                break;
        }

        log.warn("SAVING BOT STATE = " + botState.getDescription());
        BotStateUtil.saveBotState(userId, chatId, botState);
        //userDataCache.setUsersCurrentBotState(userId, botState);
        UserEntity userEntity = userEntityRepository.findByUserIdAndChatId(userId, chatId);
        if (userEntity == null && !botState.getDescription().equals("SHOW_MAIN_MENU")) {
            return new SendMessage(chatId, "К сожалению у вас нет доступа к этой информации....");
        }

        replyMessage = botStateContext.processInputMessage(botState, userId, chatId, inputMsg);
        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");

        //userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        BotStateUtil.saveBotState(userId, chatId, BotState.SHOW_MAIN_MENU);
        return callBackAnswer;

    }

    public String sendRecordToUsers(RecordData recordData) {
        if (multipleMessageSender.sendToTelegram(recordData)) {
            return "Successfully sent messages!";
        }
        return "Something went wrong!";
    }
}
