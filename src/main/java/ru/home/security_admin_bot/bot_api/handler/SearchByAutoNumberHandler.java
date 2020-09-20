package ru.home.security_admin_bot.bot_api.handler;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.InputMessageHandler;
import ru.home.security_admin_bot.dao.RecordDataEntity;
import ru.home.security_admin_bot.dao.repository.RecordDataRepository;
import ru.home.security_admin_bot.service.ReplyMessageService;
import ru.home.security_admin_bot.util.BotStateUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SearchByAutoNumberHandler implements InputMessageHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SearchByAutoNumberHandler.class);
    private final ReplyMessageService replyMessageService;
    private final RecordDataRepository recordDataRepository;

    public SearchByAutoNumberHandler(ReplyMessageService replyMessageService, RecordDataRepository recordDataRepository) {
        this.replyMessageService = replyMessageService;
        this.recordDataRepository = recordDataRepository;
    }

    @Override
    public SendMessage handle(int userId, long chatId, String text) {
        return processUsersInput(userId, chatId, text, BotStateUtil.getBotState(userId, chatId));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SEARCH_BY_AUTO_NUMBER;
    }

    private SendMessage processUsersInput(int userId, long chatId, String userAnswer, BotState botState) {
        SendMessage sendMessage = null;

        switch (botState) {
            case ASK_AUTO_NUMBER:
                sendMessage = replyMessageService.getReplyMessage(chatId, "reply.askAutoNumber");
                botState = BotState.SEARCH_BY_AUTO_NUMBER_COMPLETED;
//                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHONE_NUMBER);
                break;
            case SEARCH_BY_AUTO_NUMBER_COMPLETED:
                Pattern pattern = Pattern.compile("^[АВЕКМНОРСТУХ][0-9]{3}[АВЕКМНОРСТУХ]{2}[0-9]{2,3}$");
                String carNumber = userAnswer.toUpperCase();
                Matcher matcher = pattern.matcher(carNumber);
                if (matcher.matches()) {
                    List<RecordDataEntity> recordDataEntityList = recordDataRepository.findTop5ByCarNumberOrderByIdDesc(carNumber);
                    sendMessage = BotStateUtil.createSendMessage(chatId, recordDataEntityList, "автомобиля " + carNumber);
                    botState = BotState.SHOW_MAIN_MENU;
                } else {
                    sendMessage = new SendMessage(chatId, "Неверный номер автомобиля! Введите заново : ");
                }

                break;
        }
        BotStateUtil.saveBotState(userId, chatId, botState);

        return sendMessage;
    }
}
