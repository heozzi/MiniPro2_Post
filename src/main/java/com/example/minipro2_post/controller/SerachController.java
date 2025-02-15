package com.example.minipro2_post.controller;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import com.example.minipro2_post.service.PostService;
import com.example.minipro2_post.service.SearchService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class SerachController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private PostRepository postRepository;

    // 메인 페이지 테스트
    @GetMapping
    public ResponseEntity<String> mainPage() {
        return ResponseEntity.ok("HELLO WORLD");
    }

    // 제목 검색
//    @PostMapping
//    public ResponseEntity<String> titleSerach (@RequestBody String title) {
//        return ResponseEntity.ok("제목 검색");
//    }


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

    // 글쓴이 검색


    @PostMapping
    public ResponseEntity<?> search(@RequestBody HashMap<String, Object> map) {
        // 검색 유형을 확인
        // 태그 검색인지, 내용 검색인지 확인
        // type: tag | msg
        // msg: 검색어
        String type = map.get("type").toString();
        String searchString = map.get("msg").toString();
        System.out.println(type + ": " + searchString);

        List<PostEntity> postEntity;
        if ("tag".equals(type)) {
            postEntity = searchService.searchByTag(searchString);
        } else {
            postEntity = searchService.searach(searchString);
        }

        if (postEntity.isEmpty()) {
            return ResponseEntity.ok("검색 결과가 없습니다");
        }
        return ResponseEntity.ok(postEntity);
    }

    // 태그검색 진행
    @PostMapping("/tag")
    public ResponseEntity<List<PostEntity>> tagSearch(@RequestBody HashMap<String, Object> map) {
        String tag = map.get("tag").toString();
        System.out.println(tag);
        List<PostEntity> posts= postRepository.findByTag(tag);
        System.out.println(posts);
        return ResponseEntity.ok(posts);
    }

    // 그룹 검색 진행
    @PostMapping("/gid")
    public ResponseEntity<List<PostEntity>> gidSearch(@RequestBody HashMap<String, Object> map) {
        Long gid = Long.parseLong(map.get("gid").toString());
        System.out.println(gid);
        List<PostEntity> posts= postRepository.findByGid(gid);
        System.out.println(posts);
        return ResponseEntity.ok(posts);
    }
}
