package ru.home.security_admin_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.home.security_admin_bot.controller.to.RecordData;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MultipleMessageSender {
    private final UserEntityRepository userEntityRepository;

    @Value("${telegrambot.urlString}")
    private String urlString;
    @Value("${telegrambot.botToken}")
    private String apiToken;

    public MultipleMessageSender(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public boolean sendToTelegram(RecordData recordData) {
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        try {
            for (UserEntity userEntity : userEntityList) {
                String chatId = userEntity.getChatId().toString();
                String text = String.format("%s%n -------------------%nНомер квартиры: %s%nНомер телефона: %s%nМарка автомобиля: %s%nНомер автомобиля: %s%n",
                        "Новая заявка", recordData.getFlatNumber(), recordData.getPhoneNumber(), recordData.getCarMark(), recordData.getCarNumber());

                urlString = String.format(urlString, apiToken, chatId, text);


                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
