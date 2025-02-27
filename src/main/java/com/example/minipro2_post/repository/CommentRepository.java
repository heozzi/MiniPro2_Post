package com.example.minipro2_post.repository;

import com.example.minipro2_post.entity.CommentEntity;
import com.example.minipro2_post.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPid(PostEntity post);

    List<CommentEntity> findByUid(PostEntity post);

    // 특정 사용자가 작성한 모든 댓글 삭제
    @Modifying
    @Transactional
    void deleteByUid(Long uid);
}
