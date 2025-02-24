package com.example.minipro2_post.repository;

import com.example.minipro2_post.entity.LikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByPidAndUid(Long pid, Long uid);

    // 특정 사용자의 모든 좋아요 삭제
    @Modifying
    @Transactional
    void deleteByUid(Long uid);

    // 특정 게시글의 모든 좋아요 삭제
    @Modifying
    @Transactional
    void deleteByPid(Long pid);
}
