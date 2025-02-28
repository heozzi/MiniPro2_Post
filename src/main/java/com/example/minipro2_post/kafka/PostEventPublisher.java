package com.example.minipro2_post.kafka;

import com.example.minipro2_post.entity.PostEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PostEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishPostEvent(PostEntity postEntity) {
        try {
            // 필요한 정보만 추출
            Map<String, Object> payload = new HashMap<>();
            payload.put("pid", postEntity.getPid());
            payload.put("uid", postEntity.getUid());
            payload.put("type", postEntity.getType());
            payload.put("gid", postEntity.getGid());
            payload.put("content", postEntity.getContent());

            String jsonMessage = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("post", jsonMessage);
            System.out.println("Kafka Post 메시지 발행: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
