package ru.home.security_admin_bot.cache;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.TelegramFacade;
import ru.home.security_admin_bot.model.LoginData;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TelegramFacade.class);
    Map<Integer, BotState> usersBotState = new HashMap<>();
    Map<Integer, LoginData> recordDataMap = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotState.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotState.get(userId);
        if (botState == null) {
            botState = BotState.FILL_LOGIN;
        }

        return botState;
    }

    @Override
    public LoginData getLoginData(int userId) {
        LoginData loginData = recordDataMap.get(userId);
        if (loginData == null) {
            loginData = new LoginData();
        }

        return loginData;
    }

    @Override
    public void saveLoginData(int userId, LoginData loginData) {
        recordDataMap.put(userId, loginData);
    }


}
