package com.example.AquaGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AquariumProducer {
    private static final String TOPIC = "aquarium-updates";

    @Autowired
    private KafkaTemplate<String, AquariumUpdateEvent> kafkaTemplate;


    public void sendUpdate(AquariumUpdateEvent event) {
        kafkaTemplate.send(TOPIC, event.getAquariumId().toString(), event);
        System.out.println("Sent event to Kafka: " + event.getAction());
    }
}

