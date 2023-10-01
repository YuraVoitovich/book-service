package io.voitovich.testmodsentask.bookservice.events.service;

public interface KafkaProducerService {


    void send(String uuid);

}
