package com.example.minipro2_post.kafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class PostEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public PostEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publishNewPostEvent(Long postId, Long userId, String content) {
        String jsonMessage = String.format(
                "{\"eventType\":\"NEW_POST\",\"postId\":%d,\"userId\":%d,\"content\":\"%s\",\"timestamp\":\"%s\"}",
                postId, userId, content, java.time.Instant.now().toString()
        );
        kafkaTemplate.send("post.created", jsonMessage);
    }
}