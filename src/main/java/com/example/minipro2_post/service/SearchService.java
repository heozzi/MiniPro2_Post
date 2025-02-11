package com.example.minipro2_post.service;

import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private PostRepository postRepository;

    public void Serach(String findString) {
        PostEntity p = postRepository.findByContent(findString);
        System.out.println(p);
    }
}
