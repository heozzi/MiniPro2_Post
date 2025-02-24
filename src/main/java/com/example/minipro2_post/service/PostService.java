package com.example.minipro2_post.service;

import com.example.minipro2_post.dto.PostDto;
import com.example.minipro2_post.entity.LikeEntity;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.kafka.LikeEventPublisher;
import com.example.minipro2_post.kafka.PostEventPublisher;
import com.example.minipro2_post.repository.CommentRepository;
import com.example.minipro2_post.repository.LikeRepository;
import com.example.minipro2_post.repository.PostRepository;
import jakarta.transaction.Transactional;
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
    private CommentRepository commentRepository;
    @Autowired
    private PostEventPublisher postEventPublisher;
    @Autowired
    private LikeEventPublisher likeEventPublisher;

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
            // 게시글 내용, 타입이 null인 경우 예외 처리
            if(postDto.getContent() == null) {
                throw new RuntimeException("게시글 내용은 필수입니다.");
            } else if(postDto.getType() == null) {
                throw new RuntimeException("게시글 타입은 필수입니다.");
            }
            // 게시글 타입이 public, group, groupOnly 중 하나가 아닌 경우 예외 처리
            else if(!postDto.getType().equals("public")
                    && !postDto.getType().equals("group")
                    &&!postDto.getType().equals("groupOnly")) {
                throw new RuntimeException("게시글 타입이 옳지 않습니다.");
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
            postEventPublisher.publishPostEvent(savedPost);
            return savedPost;
        } catch (DataAccessException e) {
            throw new RuntimeException("게시글 저장 중 오류가 발생했습니다.", e);
        }
    }

    // 게시글 수정
    public Optional<PostEntity> modifyPost(Long pid, PostDto postDto, Long result) {
        return postRepository.findById(pid)
                .filter(postEntity -> postEntity.getUid().equals(result))
                .map(post -> {
                    // 게시글 타입 검증
                    if (postDto.getType() != null &&
                            !postDto.getType().equals("public") &&
                            !postDto.getType().equals("group") &&
                            !postDto.getType().equals("groupOnly")) {
                        throw new IllegalArgumentException("게시글 타입이 옳지 않습니다.");
                    }

                    Optional.ofNullable(postDto.getContent()).ifPresent(post::setContent);
                    Optional.ofNullable(postDto.getTag()).ifPresent(post::setTag);
                    Optional.ofNullable(postDto.getType()).ifPresent(post::setType);
                    Optional.ofNullable(postDto.getImage()).ifPresent(post::setImage);
                    return postRepository.save(post);
                });
    }

    // 게시글 삭제
    public boolean deletePost(Long pid, Long result) {
        return postRepository.findById(pid)
                .filter(postEntity -> postEntity.getUid().equals(result)) // UID가 일치하는지 필터링
                .map(post -> {
                    postRepository.delete(post); // 게시글 삭제
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않거나 삭제할 권한이 없습니다."));
    }

    // 특정 사용자의 모든 게시글 및 관련 댓글 삭제 (회원탈퇴용)
    // 특정 사용자의 모든 게시글 및 관련 데이터 삭제 (회원탈퇴용)
    @Transactional
    public int deleteAllByUser(Long uid) {
        int deletedPostsCount = 0;

        try {
            // 1. 해당 사용자가 작성한 게시글 찾기
            List<PostEntity> userPosts = postRepository.findByUid(uid);
            deletedPostsCount = userPosts.size();

            // 2. 해당 사용자가 작성한 다른 게시글에 달린 댓글 삭제
            for (PostEntity post : userPosts) {
                Long postId = post.getPid();
                System.out.println("삭제할 게시글 ID: " + postId);

                // 게시글의 좋아요 정보 삭제
                likeRepository.deleteByPid(postId);
            }

            // 3. 다른 사용자 게시글에 작성한 댓글 삭제
            commentRepository.deleteByUid(uid);
            System.out.println("사용자 " + uid + "가 작성한 댓글 삭제 완료");

            // 4. 해당 사용자의 모든 좋아요 정보 삭제
            likeRepository.deleteByUid(uid);
            System.out.println("사용자 " + uid + "의 좋아요 정보 삭제 완료");

            // 5. 해당 사용자의 모든 게시글 삭제
            // CascadeType.REMOVE 설정이 되어 있으면 연관된 댓글은 자동 삭제됨
            postRepository.deleteByUid(uid);
            System.out.println("사용자 " + uid + "의 게시글 삭제 완료");

            return deletedPostsCount;
        } catch (Exception e) {
            System.err.println("사용자 데이터 삭제 중 오류: " + e.getMessage());
            throw new RuntimeException("사용자 데이터 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 좋아요 토글
    public Optional<PostEntity> toggleLike(Long pid, Long uid) {
        return postRepository.findById(pid)
                .map(existingPost -> {
                    Optional<LikeEntity> likeEntity = likeRepository.findByPidAndUid(pid, uid);

                    if (likeEntity.isPresent()) {
                        existingPost.setYouLike(existingPost.getYouLike() - 1);
                        likeRepository.delete(likeEntity.get());
                    } else {
                        existingPost.setYouLike(existingPost.getYouLike() + 1);
                        likeRepository.save(new LikeEntity(null, pid, uid));
                        // 이벤트 발행
                        likeEventPublisher.publishLikeEvent(pid, uid);
                    }

                    return postRepository.save(existingPost);
                });
    }

}