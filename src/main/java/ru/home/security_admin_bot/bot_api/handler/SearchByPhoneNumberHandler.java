package ru.home.security_admin_bot.bot_api.handler;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.InputMessageHandler;
import ru.home.security_admin_bot.dao.repository.RecordDataRepository;
import ru.home.security_admin_bot.service.ReplyMessageService;
import ru.home.security_admin_bot.util.BotStateUtil;

@Component
public class SearchByPhoneNumberHandler implements InputMessageHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SearchByPhoneNumberHandler.class);
    private final ReplyMessageService replyMessageService;
    private final RecordDataRepository recordDataRepository;
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public SearchByPhoneNumberHandler(ReplyMessageService replyMessageService, RecordDataRepository recordDataRepository) {
        this.replyMessageService = replyMessageService;
        this.recordDataRepository = recordDataRepository;
    }

    @Override
    public SendMessage handle(int userId, long chatId, String text) {
        return processUsersInput(userId, chatId, text, BotStateUtil.getBotState(userId, chatId));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SEARCH_BY_PHONE_NUMBER;
    }

    private SendMessage processUsersInput(int userId, long chatId, String userAnswer, BotState botState) {
        SendMessage sendMessage = null;

        switch (botState) {
            case ASK_PHONE_NUMBER:
                sendMessage = replyMessageService.getReplyMessage(chatId, "reply.askPhoneNumber");
                botState = BotState.SEARCH_BY_PHONE_NUMBER_COMPLETED;
//                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHONE_NUMBER);
                break;
            case SEARCH_BY_PHONE_NUMBER_COMPLETED:
                try {
                    Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(userAnswer, "RU");
                    if (phoneUtil.isValidNumber(phoneNumberProto)) {
                        String phoneNumber = userAnswer.replaceAll("[\\D]", "");
                        if (phoneNumber.startsWith("7")) {
                            phoneNumber = phoneNumber.replaceFirst("7", "8");
                        }
                        sendMessage = BotStateUtil.createSendMessage(chatId,
                                recordDataRepository.findTop5ByPhoneNumberOrderByIdDesc(phoneNumber), "телефона " + phoneNumber);
                        botState = BotState.SHOW_MAIN_MENU;
                    } else {
                        sendMessage = new SendMessage(chatId, "Неверный номер телефона! Введите телефон заново : ");
                    }
                } catch (NumberParseException e) {
                    sendMessage = new SendMessage(chatId, "Неверный номер телефона! Введите телефон заново : ");
                }
                break;
        }
        BotStateUtil.saveBotState(userId, chatId, botState);

        return sendMessage;
    }
}
