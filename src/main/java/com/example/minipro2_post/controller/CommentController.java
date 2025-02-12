package com.example.minipro2_post.controller;

import com.example.minipro2_post.dto.CommentDto;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 관련 컨틀로러 진행
 * 테이블 : pid,uid,comment
 *
 * 1. 댓글 달기
 *  ㄴ pid를 확인하여 문제없는 확인
 * 2. 댓글 수정
 * ㄴ pid를 확인하여 문제없는 확인
 * 3. 댓글 삭제
 * ㄴ pid를 확인하여 문제없는 확인
 */


@RestController
@RequestMapping("/cmt")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // cmt 페이지 확인
    @GetMapping
    public ResponseEntity<String> mainPage() {
        return ResponseEntity.ok("Hello World");
    }
    // 댓글 생성 진행
    @PostMapping("create/{cid}")
    public ResponseEntity<String> createComment(
            @PathVariable Integer cid,
            @RequestBody CommentDto commentDto) {
        commentService.addComment(commentDto);
        return ResponseEntity.ok("댓글 저장완료");
    }
    // 댓글 수정 진행
    @PostMapping("modify/{cid}")
    public ResponseEntity<String> modifyComment(
            @PathVariable Integer cid
            , @RequestBody CommentDto commentDto) {
        commentService.modifyComment(commentDto,cid);
        return ResponseEntity.ok("수정완료");
    }
    // 댓글 삭제 진행
    @PostMapping("delete/{cid}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer cid) {
        commentService.deleteComment(cid);
        return ResponseEntity.ok("댓글 삭제완료");
    }
}
