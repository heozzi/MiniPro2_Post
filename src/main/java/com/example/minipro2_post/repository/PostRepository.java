package com.example.minipro2_post.repository;

import com.example.minipro2_post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // Containing을 사용하면 "% %" 효과를 얻게됨
    List<PostEntity> findByContentContaining (String findString);

    // 이름 검색


    // 태그 검색
    List<PostEntity> findByTagContaining(String tag);
}
