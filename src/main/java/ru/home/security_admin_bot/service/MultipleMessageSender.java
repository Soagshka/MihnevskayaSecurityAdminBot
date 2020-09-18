package ru.home.security_admin_bot.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.home.security_admin_bot.controller.to.RecordData;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MultipleMessageSender {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MultipleMessageSender.class);
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
                String text = "Новая заявка  \n----------------------------------------\n Номер квартиры: " + recordData.getFlatNumber() + "\n Номер телефона: " + recordData.getPhoneNumber().replaceAll("\\+", "")
                        + "\n Марка автомобиля: " + recordData.getCarMark() + "\n Номер телефона: ";
                RestTemplate restTemplate = new RestTemplate();
                StringBuilder builder = new StringBuilder("http://");
                builder.append("https://api.telegram.org/bot" + apiToken + "/sendMessage");
                builder.append("?chat_id=");
                builder.append(chatId);
                builder.append("?text=");
                builder.append(text);

                URI uri = URI.create(builder.toString());
                restTemplate.getForObject(uri, Object.class);
//                UriComponentsBuilder telegramRequestBuilder = UriComponentsBuilder.fromHttpUrl("https://api.telegram.org/bot" + apiToken + "/sendMessage")
//                        .queryParam("chat_id", chatId)
//                        .queryParam("text", text);
//                ResponseEntity<String> response
//                        = restTemplate.getForEntity(telegramRequestBuilder.toUriString(), String.class);
//                log.warn("urlString = " + urlString);
//                urlString = String.format(urlString, apiToken, chatId, text);
//
//                URL url = new URL(urlString);
//                log.warn("URL = " + url);
//                URLConnection conn = url.openConnection();
//                InputStream is = new BufferedInputStream(conn.getInputStream());
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
