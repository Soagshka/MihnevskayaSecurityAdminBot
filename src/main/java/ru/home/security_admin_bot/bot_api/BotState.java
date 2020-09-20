package ru.home.security_admin_bot.bot_api;

public enum BotState {
    SHOW_MAIN_MENU("SHOW_MAIN_MENU"),
    FILL_LOGIN("FILL_LOGIN"),
    ASK_LOGIN("ASK_LOGIN"),
    ASK_PASSWORD("ASK_PASSWORD"),
    LOGIN_SUCCESSFULLY("LOGIN_SUCCESSFULLY"),
    SHOW_10_LAST_RECORDS("SHOW_10_LAST_RECORDS"),
    SHOW_HELP("SHOW_HELP");

    private final String description;

    BotState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
