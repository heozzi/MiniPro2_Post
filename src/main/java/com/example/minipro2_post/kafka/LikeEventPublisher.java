package com.example.minipro2_post.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LikeEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public LikeEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishLikeEvent(Long pid, Long uid) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("pid", pid);
            payload.put("uid", uid);

            String jsonMessage = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("like", jsonMessage);
            System.out.println("Kafka Like 메시지 발행: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
