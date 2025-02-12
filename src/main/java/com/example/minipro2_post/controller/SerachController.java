package com.example.minipro2_post.controller;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 게시글 검색
 *
 * 1. 제목 검색 -> 인스타그램에는 제목이 없음 그러니 PASS
 * 2. 글쓴이 검색
 * 3. 내용 검색
 * 4. 제목 + 내용 검색
 */

@RestController
@RequestMapping("/search")
public class SerachController {
    @Autowired
    private SearchService searchService;

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


    // 내용 검색
    @PostMapping
    public ResponseEntity<List<PostEntity>> Serach(@RequestBody HashMap<String, Object> map) {
        // 오브젝트를 string으로 변환
        String findString = map.get("msg").toString();
        System.out.println(findString);

        List<PostEntity> postEntity =  searchService.serach(findString);
        return ResponseEntity.ok(postEntity);
    }
    // 제목+내용 검색
}
