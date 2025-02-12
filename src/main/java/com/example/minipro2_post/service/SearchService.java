package com.example.minipro2_post.service;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


    public List<PostEntity> serach(String findString) {
        List<PostEntity> contentSerach = postRepository.findByContentContaining(findString);

        for (PostEntity postEntity : contentSerach) {
            System.out.println(postEntity);
        }

        return contentSerach;
    }
}
