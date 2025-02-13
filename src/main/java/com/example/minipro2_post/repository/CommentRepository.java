package com.example.minipro2_post.repository;

import com.example.minipro2_post.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
