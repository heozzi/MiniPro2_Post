package com.example.minipro2_post.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 게시글 이벤트 발행
 */
@Service
public class PostEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public PostEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publishNewPostEvent(Long pid, Long uid, String content) {
        String jsonMessage = String.format(
                "{\"eventType\":\"NEW_POST\",\"pid\":%d,\"uid\":%d,\"content\":\"%s\",\"timestamp\":\"%s\"}",
                pid, uid, content, java.time.Instant.now().toString()
        );
        kafkaTemplate.send("post.created", jsonMessage);
    }

}
