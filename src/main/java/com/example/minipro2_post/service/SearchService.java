package com.example.minipro2_post.service;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 검색기능
 * 1. 이름 검색
 * 2. 내용 검색
 * 3. 태그 검색
 */
@Service
public class SearchService {
    @Autowired
    private PostRepository postRepository;


    public List<PostEntity> searach(String findString) {
        List<PostEntity> contentSerach = postRepository.findByContentContaining(findString);

        for (PostEntity postEntity : contentSerach) {
            System.out.println(postEntity);
        }

        return contentSerach;
    }

    // 이름 검색

    // 태그 검색
    public List<PostEntity> searchByTag(String tag) {
        return postRepository.findByTag(tag);
    }
}
