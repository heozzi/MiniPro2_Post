package com.example.minipro2_post.service;

import com.example.minipro2_post.dto.CommentDto;
import com.example.minipro2_post.entity.CommentEntity;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.CommentRepository;
import com.example.minipro2_post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private PostRepository postRepository;

    // 댓글 추가
    public void addComment(CommentDto commentDto, Long pid, Long uid) {
        Optional<PostEntity> postEntity = postRepository.findById(pid);

        if (postEntity.isPresent()) {
            if (!postEntity.get().getUid().equals(uid)) {
                throw new IllegalArgumentException("UID가 일치하지 않습니다!");
            }
            CommentEntity commentEntity = CommentEntity.builder()
                    .pid(postEntity.get())
                    .uid(commentDto.getUid())
                    .content(commentDto.getContent())
                    .uid(uid)
                    .build();
            commentRepository.save(commentEntity);
        } else {
            throw new IllegalArgumentException("해당 게시글은 존재하지 않습니다.");
        }
    }
    // 댓글 수정
    public void modifyComment(CommentDto commentDto, Long cid, Long uid) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(cid);
        if (commentEntity.isPresent()) {
            if (!commentEntity.get().getUid().equals(uid)) {
                throw new IllegalArgumentException("UID가 일치하지 않습니다!");
            }
            commentEntity.get().setContent(commentDto.getContent());
            commentRepository.save(commentEntity.get());
        }
        else {
            throw new IllegalArgumentException("해당 댓글은 존재하지 않습니다.");
        }
    }
    // 댓글 삭제
    public void deleteComment(Long cid, Long uid) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(cid);
        if (commentEntity.isPresent()) {
            if (!commentEntity.get().getUid().equals(uid)) {
                throw new IllegalArgumentException("UID가 일치하지 않습니다!");
            }
            commentRepository.deleteById(cid);
        }
    }

    // 댓글 조회
    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(comment ->
                    CommentDto.builder()
                            .uid(comment.getUid())
                            .content(comment.getContent())
                            .build()
                )
                .collect(Collectors.toList());
    }

    // 특정 pid에 해당하는 댓글 조회
    public List<CommentDto> getPidComments(Long pid) {
        // PostEntity 찾기
        PostEntity post = postRepository.findById(pid).orElseThrow();

        List<CommentEntity> comments = commentRepository.findByPid(post);
        return comments.stream()
                .map(comment -> {
                    return CommentDto.builder()
                            .uid(comment.getUid())
                            .content(comment.getContent()) // 여기서 오류 발생?
                            .build();
                })
                .collect(Collectors.toList());
    }

}
