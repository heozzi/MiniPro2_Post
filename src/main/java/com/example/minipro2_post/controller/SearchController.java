package com.example.minipro2_post.controller;

import com.example.minipro2_post.dto.PostDto;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import com.example.minipro2_post.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 게시글 검색
 * 1. 내용 검색
 * 2. 글쓴이 검색
 * 3. 태그 검색
 */

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;


    // 메인 페이지 테스트
    @GetMapping
    public ResponseEntity<String> mainPage() {
        return ResponseEntity.ok("HELLO WORLD");
    }

//    // 내용 검색
//    @PostMapping
//    public ResponseEntity<List<PostEntity>> Searach(@RequestBody HashMap<String, Object> map) {
//        // 오브젝트를 string으로 변환
//        String findString = map.get("msg").toString();
//        System.out.println(findString);
//
//        List<PostEntity> postEntity =  searchService.searach(findString);
//        return ResponseEntity.ok(postEntity);
//    }

    // 게시글 검색 : 태그, 내용, 이메일
    @PostMapping
    public ResponseEntity<?> search(@RequestBody HashMap<String, Object> map) {
        // 검색 유형을 확인
        String type = map.get("type").toString();
        String searchString = map.get("msg").toString();
        System.out.println(type + ": " + searchString);

        List<PostDto> postDtoList = null;

        try {
            if("tag".equals(type)) {
                postDtoList = searchService.searchByTag(searchString);
            } else if
            ("msg".equals(type)) {
                postDtoList = searchService.search(searchString);
            } else if ("email".equals(type)) {
                postDtoList = searchService.searchByEmail(searchString);
            }
        } catch (Exception e) {
            return ResponseEntity.ok("해당 이메일을 가진 사용자가 없습니다.");
        }
        List<Long> postIds = new ArrayList<>();
        for (PostDto postDto : postDtoList) {
            if (!postDto.getGid().equals(0)) {
                postIds.add(postDto.getGid());
            }
        }
        for (int i = postIds.size() - 1; i >= 0; i--) {
            postDtoList.remove(postIds.get(i));
        }

        if (postDtoList == null || postDtoList.isEmpty()) {
            return ResponseEntity.ok("검색 결과가 없습니다");
        }
        return ResponseEntity.ok(postDtoList);
    }


    // 피드의 태그 검색 진행
    @PostMapping("/tag")
    public ResponseEntity<List<PostDto>> tagSearch(@RequestBody HashMap<String, Object> map) {
        String tag = map.get("tag").toString(); // JSON에서 태그 값 추출
        System.out.println("검색할 태그: " + tag);

        List<PostDto> posts = searchService.searchByTag(tag);

        System.out.println("검색된 게시글: " + posts);
        return ResponseEntity.ok(posts);
    }


    // 피드의 그룹 검색 진행
    @PostMapping("/gid")
    public ResponseEntity<List<PostEntity>> gidSearch(@RequestBody HashMap<String, Object> map) {
        Long gid = Long.parseLong(map.get("gid").toString());
        System.out.println(gid);
        List<PostEntity> posts= postRepository.findByGid(gid);
        System.out.println(posts);
        return ResponseEntity.ok(posts);
    }
}
