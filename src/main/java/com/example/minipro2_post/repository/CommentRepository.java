package com.example.minipro2_post.repository;

import com.example.minipro2_post.entity.CommentEntity;
import com.example.minipro2_post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPid(PostEntity post);
}
