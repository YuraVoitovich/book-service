package io.voitovich.testmodsentask.bookservice.events.service.impl;

import io.voitovich.testmodsentask.bookservice.events.service.KafkaProducerService;
import io.voitovich.testmodsentask.bookservice.exception.KafkaSendingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = "librarytopic";
    }

    @Override
    public void send(String uuid) {
        try {
            kafkaTemplate.send(topic, uuid);
            log.info("Message sent successfully to topic: {} with UUID: {}", topic, uuid);
        } catch (Exception e) {
            log.error("Error while sending message to topic: {} with UUID: {}", topic, uuid, e);
            throw new KafkaSendingException("Error while sending message to kafka", e);
        }
    }
}
