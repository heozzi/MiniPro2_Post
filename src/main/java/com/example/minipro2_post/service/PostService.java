package com.example.minipro2_post.service;

import com.example.minipro2_post.dto.PostDto;
import com.example.minipro2_post.entity.LikeEntity;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.repository.LikeRepository;
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
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostEventPublisher postEventPublisher;

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
                    .like(post.getYouLike())
                    .tag(post.getTag())
                    .image(post.getImage())
                    // null 체크 후 댓글 개수 설정 (댓글이 없을 경우 0으로 설정)
                    .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                    .build());
        }
        return postDtos;
    }

    // 게시글 생성
    public PostEntity createPost(PostDto postDto, Long result) {
        try {
            if(postDto.getUid() == null || postDto.getContent() == null) {
                throw new RuntimeException("작성자 번호와 게시글 내용은 필수입니다.");
            }
            PostEntity postEntity = PostEntity.builder()
                    .uid(result)
                    .type(postDto.getType())
                    .gid(postDto.getGid() != null ? postDto.getGid() : 0L)
                    .date(postDto.getDate() != null ? postDto.getDate() : LocalDateTime.now())
                    .content(postDto.getContent())
                    .youLike(postDto.getLike() != null ? postDto.getLike() : 0L)
                    .tag(postDto.getTag())
                    .image(postDto.getImage())
                    .build();

            // 게시물 생성
            PostEntity savedPost = postRepository.save(postEntity);
            // 이벤트 발행
            postEventPublisher.publishNewPostEvent(savedPost.getPid(), savedPost.getUid(), savedPost.getContent());
            return savedPost;
        } catch (DataAccessException e) {
            throw new RuntimeException("게시글 저장 중 오류가 발생했습니다.", e);
        }
    }

    // 게시글 수정
    public Optional<PostEntity> modifyPost(Long pid, PostDto postDto) {
        return postRepository.findById(pid)
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
    public void deletePost(Long pid) {
        if(!postRepository.existsById(pid)) {
            throw new RuntimeException("해당 게시글이 존재하지 않습니다.");
        }
        postRepository.deleteById(pid);
    }

    // 좋아요 토글
    public Optional<PostEntity> toggleLike(Long pid, Long uid) {
        Optional<LikeEntity> likeEntity = likeRepository.findByPidAndUid(pid, uid);
        return postRepository.findById(pid)
                .map(existingPost -> {
                    if (likeEntity.isPresent()) {
                        existingPost.setYouLike(existingPost.getYouLike() - 1);
                        likeRepository.delete(likeEntity.get());
                    } else {
                        existingPost.setYouLike(existingPost.getYouLike() + 1);
                        likeRepository.save(new LikeEntity(null, pid, uid));
                    }
                    return postRepository.save(existingPost);
                });
    }

}