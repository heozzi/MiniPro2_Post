package com.example.minipro2_post.repository;

import com.example.minipro2_post.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByPidAndUid(Long pid, Long uid);
}
