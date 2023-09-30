package io.voitovich.testmodsentask.bookservice.events.source.impl;

import io.voitovich.testmodsentask.bookservice.events.source.KafkaProducerService;
import io.voitovich.testmodsentask.bookservice.exception.KafkaSendingException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


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
        } catch (Exception e) {
                throw new KafkaSendingException("Error while sending message to kafka");
        }
    }
}
