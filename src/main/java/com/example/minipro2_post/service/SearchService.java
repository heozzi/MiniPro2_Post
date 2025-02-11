package com.example.minipro2_post.service;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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


    public void serach(String findString) {
        PostEntity p = postRepository.findByContent(findString);
        List<PostEntity> p2 = postRepository.findByContentContaining(findString);

        System.out.println(p);
        for (PostEntity x : p2) {
            System.out.println(x);
        }
    }
}
