package ru.home.security_admin_bot.cache;

import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.model.LoginData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    LoginData getLoginData(int userId);

    void saveLoginData(int userId, LoginData recordData);
}
