package ru.mdimension.stand_bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class RabbitMQService {

    private final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;


    public <T> T convertMessageBodyToObject(Message message, Class<T> cls) throws JsonConversionException {
        return convertMessageToObject(message, cls, null);
    }

    private <T> T convertMessageToObject(Message message, Class<T> cls,
                                         CollectionType collectionType)
            throws JsonConversionException {

        if (message == null) {
            return null;
        }
        byte[] result = message.getBody();
        if (result == null) {
            return null;
        }
        String json = new String(result, StandardCharsets.UTF_8);
        if (json.isEmpty()) {
            return null;
        }
        T value;
        try {
            if (cls != null) {
                value = objectMapper.readValue(json, cls);
            } else if (collectionType != null) {
                value = objectMapper.readValue(json, collectionType);
            } else {
                throw new JsonConversionException("No class or type found");
            }
        } catch (IOException e) {
            throw new JsonConversionException(e);
        }
        return value;
    }

    public <T> T getObjectFromMessage(Message message, Class<T> tClass) {
        byte[] msgBytes = message.getBody();
        msgBytes = (msgBytes == null) ? new byte[0] : msgBytes;
        String msgString = new String(msgBytes, StandardCharsets.UTF_8);
        if (msgString.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(msgString, tClass);
        } catch (IOException e) {
            log.error("Couldn't read timeLeftMessage to object because of {}: {}",
                    e.getClass().getSimpleName(), e.getMessage());
            return null;
        }
    }

    public void convertAndSend(String exchangeName, String routingName, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingName, createMessage(message));
    }

    private Message createMessage(Object dto) {
        byte[] body;
        try {
            body = objectMapper.writeValueAsBytes(dto);
        } catch (IOException e) {
            throw new JsonConversionException(e);
        }
        return new Message(body, new MessageProperties());
    }
}
