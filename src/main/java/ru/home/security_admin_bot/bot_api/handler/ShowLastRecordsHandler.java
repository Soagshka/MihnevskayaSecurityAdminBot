package ru.home.security_admin_bot.bot_api.handler;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.security_admin_bot.bot_api.BotState;
import ru.home.security_admin_bot.bot_api.InputMessageHandler;
import ru.home.security_admin_bot.dao.RecordDataEntity;
import ru.home.security_admin_bot.dao.repository.RecordDataRepository;
import ru.home.security_admin_bot.util.BotStateUtil;

import java.util.List;

@Component
public class ShowLastRecordsHandler implements InputMessageHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ShowLastRecordsHandler.class);
    private final RecordDataRepository recordDataRepository;

    public ShowLastRecordsHandler(RecordDataRepository recordDataRepository) {
        this.recordDataRepository = recordDataRepository;
    }

    @Override
    public SendMessage handle(int userId, long chatId, String text) {
        SendMessage sendMessage;
        List<RecordDataEntity> recordDataEntityList = recordDataRepository.findTop5ByOrderById();
        sendMessage = BotStateUtil.createSendMessage(chatId, recordDataEntityList);

        BotStateUtil.saveBotState(userId, chatId, BotState.SHOW_MAIN_MENU);
        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_5_LAST_RECORDS;
    }
}
