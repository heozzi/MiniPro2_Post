package com.example.minipro2_post.repository;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.entity.PostTagEntity;
import com.example.minipro2_post.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {
    // 특정 게시글과 태그를 삭제하는 용도
    void deleteByPostAndTag(PostEntity post, TagEntity tag);

    // 태그가 게시글과 연결되어 있는지 확인하는 용도
    int countByTag(TagEntity tag);

    // uid를 통해 게시글 삭제
    void deleteByPost(PostEntity post);

    // tag로 검색
    List<PostTagEntity> findByTag(TagEntity tag);

}
