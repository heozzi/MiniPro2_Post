package com.example.minipro2_post.service;

import com.example.minipro2_post.dto.CommentDto;
import com.example.minipro2_post.entity.CommentEntity;
import com.example.minipro2_post.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 댓글 시스템의 전반적인 과정을 담음
 * 1. 댓글 달기
 * 가장 중요한건, pid와 uid를 가져오기
 * 2. 댓글 수정
 * 3. 댓글 삭제
 */


@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    // 댓글 추가
    public void addComment(CommentDto commentDto) {
        CommentEntity commentEntity;
        commentEntity = CommentEntity.builder()
                .cid(commentDto.getCid())
                .content(commentDto.getContent())
                .build();
        commentRepository.save(commentEntity);
    }
    // 댓글 수정
    public void modifyComment(CommentDto commentDto, Integer cid) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(cid);
        if (commentEntity.isPresent()) {
            commentEntity.get().setContent(commentDto.getContent());
            commentRepository.save(commentEntity.get());
        }
        else {
            throw new IllegalArgumentException("해당 댓글은 존재하지 않습니다.");
        }
    }
    // 댓글 삭제
    public void deleteComment(Integer cid) {
        commentRepository.deleteById(cid);
    }
}
