package com.example.minipro2_post.kafka;

import com.example.minipro2_post.entity.CommentEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommentEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishCommentEvent(CommentEntity commentEntity) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("cid", commentEntity.getCid());
            payload.put("pid", commentEntity.getPid().getPid());
            payload.put("uid", commentEntity.getUid());
            payload.put("content", commentEntity.getContent());

            String jsonMessage = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("comment", jsonMessage);
            System.out.println("Kafka Comment 메시지 발행: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
