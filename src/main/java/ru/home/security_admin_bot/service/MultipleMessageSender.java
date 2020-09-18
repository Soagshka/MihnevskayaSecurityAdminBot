package ru.home.security_admin_bot.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.home.security_admin_bot.controller.to.RecordData;
import ru.home.security_admin_bot.dao.UserEntity;
import ru.home.security_admin_bot.dao.repository.UserEntityRepository;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
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

                URI uri = URI.create("https://api.telegram.org/bot" + apiToken + "/sendMessage" + "?chat_id=" + chatId + "?text=" + URLEncoder.encode(
                        "urlParameterString",
                        java.nio.charset.StandardCharsets.UTF_8.toString()
                ));
                log.warn("URI = " + uri.toString());
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                headers.add(HttpHeaders.ACCEPT, "application/json");
                HttpEntity<?> entity = new HttpEntity<Object>(headers);
                HttpEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

//                restTemplate.getForObject(uri, Object.class);
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
        } catch (InterruptedException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
