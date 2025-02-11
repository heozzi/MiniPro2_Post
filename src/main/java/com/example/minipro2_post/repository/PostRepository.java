package com.example.minipro2_post.repository;


import com.example.minipro2_post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity,Integer>{
    PostEntity findBySubject (String subject);
    PostEntity findByContent (String content);

//    List<PostEntity> findAll(String subject);
}
