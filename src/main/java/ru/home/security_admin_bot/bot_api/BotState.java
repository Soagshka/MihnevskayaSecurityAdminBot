package ru.home.security_admin_bot.bot_api;

public enum BotState {
    SHOW_MAIN_MENU("SHOW_MAIN_MENU"),
    FILL_LOGIN("FILL_LOGIN"),
    ASK_LOGIN("ASK_LOGIN"),
    ASK_PASSWORD("ASK_PASSWORD"),
    LOGIN_SUCCESSFULLY("LOGIN_SUCCESSFULLY"),
    SHOW_5_LAST_RECORDS("SHOW_5_LAST_RECORDS"),
    SEARCH_BY_AUTO_NUMBER("SEARCH_BY_AUTO_NUMBER"),
    ASK_AUTO_NUMBER("ASK_AUTO_NUMBER"),
    SEARCH_BY_AUTO_NUMBER_COMPLETED("SEARCH_BY_AUTO_NUMBER_COMPLETED"),
    SEARCH_BY_PHONE_NUMBER("SEARCH_BY_PHONE_NUMBER"),
    ASK_PHONE_NUMBER("ASK_PHONE_NUMBER"),
    SEARCH_BY_PHONE_NUMBER_COMPLETED("SEARCH_BY_PHONE_NUMBER_COMPLETED"),
    SHOW_HELP("SHOW_HELP");

    private final String description;

    BotState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
