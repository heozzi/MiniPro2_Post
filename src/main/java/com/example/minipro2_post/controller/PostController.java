package com.example.minipro2_post.controller;

import com.example.minipro2_post.dto.PostDto;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    // DI
    @Autowired
    private PostService postService;
    @Autowired
    private WebClient.Builder webClientBuilder;

    // 테스트용 전체 게시글 출력
    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPost() {
        return ResponseEntity.ok(postService.getAllPost());
    }

    // 게시글 작성
    @PostMapping("/create")
    public ResponseEntity<PostEntity> createPost(@RequestBody PostDto postDto,
                                                 @RequestHeader("X-Auth-User") String email) {
//        Mono<Long> webClient = webClientBuilder.baseUrl("http://localhost:8083").build()
//                .post().uri("/user/checkemail").body(BodyInserters.fromFormData("email",email)).retrieve()
//                .bodyToMono(Long.class);
//        webClient.subscribe(res -> System.out.println(res));
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    // 게시글 수정
    @PutMapping("/modify/{pid}")
    public ResponseEntity<PostEntity> modifyPost(@PathVariable Long pid, @RequestBody PostDto postDto) {
        return postService.modifyPost(pid, postDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{pid}")
    public ResponseEntity<String> deletePost(@PathVariable Long pid) {
        postService.deletePost(pid);
        return ResponseEntity.ok(pid + "번 게시글 삭제 완료");
    }

    // 좋아요 토글
    @PutMapping("/toggleLike/{pid}/{uid}")
    public ResponseEntity<String> toggleLike(@PathVariable Long pid, @PathVariable Long uid) {
        return postService.toggleLike(pid, uid)
                .map(post -> ResponseEntity.ok(post.getYouLike() + ": 좋아요 상태 변경"))
                .orElse(ResponseEntity.notFound().build());
    }
}