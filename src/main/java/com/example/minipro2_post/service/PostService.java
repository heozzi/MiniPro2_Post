package com.example.minipro2_post.service;

import com.example.minipro2_post.dto.PostDto;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    // DI
    @Autowired
    private PostRepository postRepository;

    // 테스트용 전체 게시글 출력
    public List<PostDto> getAllPost() {
        List<PostEntity> posts = postRepository.findAll();
        List<PostDto> postDtos = new ArrayList<>();
        for (PostEntity post : posts) {
            postDtos.add(PostDto.builder()
                    .pid(post.getPid())
                    .uid(post.getUid())
                    .type(post.getType())
                    .gid(post.getGid())
                    .date(post.getDate())
                    .content(post.getContent())
                    .cid(post.getCid())
                    .like(post.getYouLike())
                    .tag(post.getTag())
                    .image(post.getImage())
                    .build());
        }
        return postDtos;
    }

    // 게시글 생성
    public PostEntity createPost(PostDto postDto) {
        try {
            if(postDto.getUid() == null || postDto.getContent() == null) {
                throw new RuntimeException("작성자 번호와 게시글 내용은 필수입니다.");
            }
            PostEntity postEntity = PostEntity.builder()
                    .uid(postDto.getUid())
                    .type(postDto.getType())
                    .gid(postDto.getGid() != null ? postDto.getGid() : 0L)
                    .date(postDto.getDate() != null ? postDto.getDate() : LocalDateTime.now())
                    .content(postDto.getContent())
                    .cid(postDto.getCid() != null ? postDto.getCid() : 0L)
                    .youLike(postDto.getLike() != null ? postDto.getLike() : 0L)
                    .tag(postDto.getTag())
                    .image(postDto.getImage())
                    .build();

            return postRepository.save(postEntity);
        } catch (DataAccessException e) {
            throw new RuntimeException("게시글 저장 중 오류가 발생했습니다.", e);
        }
    }

    // 게시글 수정
    public Optional<PostEntity> modifyPost(Long postId, PostDto postDto) {
        return postRepository.findById(postId)
                .map(existingPost -> {
                    if (postDto.getContent() != null) {
                        existingPost.setContent(postDto.getContent());
                    }
                    if (postDto.getTag() != null) {
                        existingPost.setTag(postDto.getTag());
                    }
                    if (postDto.getType() != null) {
                        existingPost.setType(postDto.getType());
                    }
                    existingPost.setDate(LocalDateTime.now()); // 수정 시간 업데이트
                    return postRepository.save(existingPost);
                });
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new RuntimeException("해당 게시글이 존재하지 않습니다.");
        }
        postRepository.deleteById(postId);
    }

    // 좋아요 개수 증가
    public Optional<PostEntity> increaseLike(Long postId) {
        return postRepository.findById(postId)
                .map(existingPost -> {
                    existingPost.setYouLike(existingPost.getYouLike() + 1);
                    return postRepository.save(existingPost);
                });
    }

    // 좋아요 개수 감소
    public Optional<PostEntity> decreaseLike(Long postId) {
        return postRepository.findById(postId)
                .map(existingPost -> {
                    existingPost.setYouLike(existingPost.getYouLike() - 1);
                    return postRepository.save(existingPost);
                });
    }

}