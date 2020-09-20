package ru.home.security_admin_bot.bot_api.handler;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.InputMessageHandler;
import ru.home.security_admin_bot.cache.UserDataCache;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;
import ru.home.security_admin_bot.model.LoginData;
import ru.home.security_admin_bot.service.ReplyMessageService;
import ru.home.security_admin_bot.util.BotStateUtil;

@Component
public class FillingLoginHandler implements InputMessageHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FillingLoginHandler.class);
    private static final String LOGIN = "ОхранаМихневская8";
    private static final String PASSWORD = "АдминМихневская8";
    private final ReplyMessageService replyMessageService;
    private final UserEntityRepository userEntityRepository;
    private final UserDataCache userDataCache;

    public FillingLoginHandler(ReplyMessageService replyMessageService, UserEntityRepository userEntityRepository, UserDataCache userDataCache) {
        this.replyMessageService = replyMessageService;
        this.userEntityRepository = userEntityRepository;
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(int userId, long chatId, String text) {
        return processUsersInput(userId, chatId, text, BotStateUtil.getBotState(userId, chatId));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILL_LOGIN;
    }

    private SendMessage processUsersInput(int userId, long chatId, String userAnswer, BotState botState) {
        UserEntity userEntity = userEntityRepository.findByUserIdAndChatId(userId, chatId);
        if (userEntity != null) {
            return new SendMessage(chatId, "Вы уже авторизовались в системе. Воспользуйтесь меню. ");
        }
        LoginData loginData = userDataCache.getLoginData(userId);

        SendMessage sendMessage = null;

        switch (botState) {
            case ASK_LOGIN:
                sendMessage = replyMessageService.getReplyMessage(chatId, "reply.askLogin");
                botState = BotState.ASK_PASSWORD;
//                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHONE_NUMBER);
                break;
            case ASK_PASSWORD:
                sendMessage = replyMessageService.getReplyMessage(chatId, "reply.askPassword");
                if (LOGIN.equals(userAnswer)) {
                    loginData.setUserName(userAnswer);
                    botState = BotState.LOGIN_SUCCESSFULLY;
//                        userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CAR_MARK);
                } else {
                    sendMessage = new SendMessage(chatId, "Неверный логин! Введите заново : ");
                }
                break;
            case LOGIN_SUCCESSFULLY:
                if (PASSWORD.equals(userAnswer)) {
                    sendMessage = replyMessageService.getReplyMessage(chatId, "reply.loginSuccessfully");
                    loginData.setPassword(userAnswer);
                    userEntityRepository.save(new UserEntity(userId, chatId));
                    botState = BotState.SHOW_MAIN_MENU;
                    //userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CAR_NUMBER);
                } else {
                    sendMessage = new SendMessage(chatId, "Неверный пароль! Введите пароль заново : ");
                }
                break;
        }
        BotStateUtil.saveBotState(userId, chatId, botState);
        userDataCache.saveLoginData(userId, loginData);

        return sendMessage;
    }
}
