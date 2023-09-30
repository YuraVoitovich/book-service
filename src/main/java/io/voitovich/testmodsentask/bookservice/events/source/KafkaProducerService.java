package io.voitovich.testmodsentask.bookservice.events.source;

public interface KafkaProducerService {


    void send(String uuid);

}
