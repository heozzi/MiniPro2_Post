package com.example.minipro2_post.service;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 검색기능
 * 1. 이메일 검색
 * 2. 내용 검색
 * 3. 태그 검색
 */
@Service
public class SearchService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // 이메일 검색
    public List<PostEntity> searchByEmail(String email) {
        try {
            // email 검색을 위한 API 호출
            Mono<Long> webClient = webClientBuilder.baseUrl("http://localhost:8083").build()
                    .post()
                    .uri("/user/checkemail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"email\":\"" + email + "\"}")
                    .retrieve()
                    .bodyToMono(Long.class);

            Long uid = webClient.block(); // API 응답이 없거나 오류 발생 시 예외 발생

            return postRepository.findByUid(uid);
        } catch (Exception e) {
            throw new RuntimeException("해당 이메일을 가진 사용자가 없습니다.");
        }
    }

    // 내용 검색
    public List<PostEntity> search(String findString) {
        List<PostEntity> contentSerach = postRepository.findByContentContaining(findString);

        for (PostEntity postEntity : contentSerach) {
            System.out.println(postEntity);
        }

        return contentSerach;
    }

    // 태그 검색
    public List<PostEntity> searchByTag(String tag) {
        return postRepository.findByTag(tag);
    }
}
