package com.example.minipro2_post.service;

import com.example.minipro2_post.dto.PostDto;
import com.example.minipro2_post.entity.LikeEntity;
import com.example.minipro2_post.entity.PostEntity;
import com.example.minipro2_post.entity.PostTagEntity;
import com.example.minipro2_post.entity.TagEntity;
import com.example.minipro2_post.kafka.LikeEventPublisher;
import com.example.minipro2_post.kafka.PostEventPublisher;
import com.example.minipro2_post.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private TagRepository tagRepository;
    @Autowired
    private PostTagRepository postTagRepository;
    @Autowired
    private PostEventPublisher postEventPublisher;
    @Autowired
    private LikeEventPublisher likeEventPublisher;

    // 게시글 생성
    public PostEntity createPost(PostDto postDto, Long result) {
        try {
            // 게시글 내용, 타입이 null인 경우 예외 처리
            if(postDto.getContent() == null) {
                throw new RuntimeException("게시글 내용은 필수입니다.");
            }
            // 게시글 타입이 public, group, groupOnly 중 하나가 아닌 경우 예외 처리
            else if(!postDto.getType().equals("public")
                    && !postDto.getType().equals("group")
                    &&!postDto.getType().equals("groupOnly")) {
                throw new RuntimeException("게시글 타입이 옳지 않거나 공백입니다.");
            }

            PostEntity postEntity = PostEntity.builder()
                    .uid(result)
                    .type(postDto.getType())
                    .gid(postDto.getType().equals("public") ?  0L : postDto.getGid())
                    .date(postDto.getDate() != null ? postDto.getDate() : LocalDateTime.now())
                    .content(postDto.getContent())
                    .youLike(postDto.getLike() != null ? postDto.getLike() : 0L)
                    .image(postDto.getImage())
                    .build();

            // 게시글 저장
            PostEntity savedPost = postRepository.save(postEntity);

            // 태그 저장을 위한 Set 생성
            Set<PostTagEntity> postTagsToAdd = new HashSet<>();

            // 태그 저장 및 PostEntity에 반영
            if (postDto.getTags() != null) {
                System.out.println(postDto.getTags());
                for (String tagName : postDto.getTags()) {
                    TagEntity tag = tagRepository.findByTagName(tagName)
                            .orElseGet(() -> tagRepository.save(new TagEntity(tagName)));

                    PostTagEntity postTag = new PostTagEntity(savedPost, tag);
                    postTagsToAdd.add(postTag);
                }
                savedPost.getPostTags().addAll(postTagsToAdd);
                postRepository.save(savedPost);  // PostEntity 저장

            }

            // 이벤트 발행
            postEventPublisher.publishPostEvent(savedPost);
            return savedPost;
        } catch (DataAccessException e) {
            throw new RuntimeException("게시글 저장 중 오류가 발생했습니다.", e);
        }
    }

    // 게시글 수정
    @Transactional
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
                    Optional.ofNullable(postDto.getType()).ifPresent(post::setType);
                    Optional.ofNullable(postDto.getImage()).ifPresent(post::setImage);

                    // 기존 태그 리스트 호출
                    Set<PostTagEntity> existingTags = new HashSet<>(Optional.ofNullable(post.getPostTags()).orElse(new HashSet<>()));
                    Set<String> existingTagNames = existingTags.stream()
                            .map(postTag -> postTag.getTag().getTagName()) // 기존 태그 이름 목록
                            .collect(Collectors.toSet());

                    // 새로운(입력된) 태그 리스트 호출
                    Set<String> newTagNames = new HashSet<>(postDto.getTags());

                    // 기존 태그에는 있지만 새로운 태그에는 없는 경우 삭제
                    Set<PostTagEntity> tagsToRemove = existingTags.stream()
                            .filter(postTag -> !newTagNames.contains(postTag.getTag().getTagName()))
                            .collect(Collectors.toSet());

                    // 추가할 태그
                    Set<PostTagEntity> tagsToAdd = new HashSet<>();
                    for (String tagName : newTagNames) {
                        if (!existingTagNames.contains(tagName)) { // 기존 태그에 없으면 추가
                            TagEntity tag = tagRepository.findByTagName(tagName)
                                    .orElseGet(() -> tagRepository.save(new TagEntity(tagName)));

                            PostTagEntity postTag = new PostTagEntity(post, tag);
                            tagsToAdd.add(postTag);
                        }
                    }

                    // 태그 관계 업데이트
                    // 특정 게시글의 태그 관계 삭제
                    for (PostTagEntity postTag : tagsToRemove) {
                        postTagRepository.deleteByPostAndTag(post, postTag.getTag()); // 특정 게시글에서만 삭제
                        post.getPostTags().remove(postTag);
                    }

                    // 사용되지 않는 태그 삭제
                    for (PostTagEntity postTag : tagsToRemove) {
                        TagEntity tag = postTag.getTag();
                        if (postTagRepository.countByTag(tag) == 0) { // 다른 게시글에서 사용되지 않으면 삭제
                            tagRepository.delete(tag);
                        }
                    }
                    // 새로운 태그 추가
                    post.getPostTags().addAll(tagsToAdd);

                    return postRepository.save(post);
                });
    }

    // 게시글 삭제
    @Transactional
    public boolean deletePost(Long pid, Long result) {
        return postRepository.findById(pid)
                .filter(postEntity -> postEntity.getUid().equals(result)) // UID가 일치하는지 필터링
                .map(post -> {
                    // 좋아요 정보 삭제
                    likeRepository.deleteByPid(post.getPid());

                    // 게시글과 연결된 PostTagEntity 찾기
                    Set<PostTagEntity> postTags = new HashSet<>(post.getPostTags());

                    // postTag 삭제
                    for (PostTagEntity postTag : postTags) {
                        postTagRepository.deleteByPostAndTag(post, postTag.getTag()); // 특정 게시글의 태그 관계만 삭제
                    }
                    post.getPostTags().clear(); // JPA에서 변경 사항 감지하도록 리스트 초기화

                    // 사용되지 않는 태그 삭제
                    for (PostTagEntity postTag : postTags) {
                        TagEntity tag = postTag.getTag();
                        if (postTagRepository.countByTag(tag) == 0) { // 더 이상 연결된 게시글이 없는 경우
                            tagRepository.delete(tag); // 태그 삭제
                        }
                    }

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

            // 2. 해당 사용자가 작성한 게시글에 대한 PostTag 삭제
            Set<TagEntity> tagsToCheck = new HashSet<>();
            for (PostEntity post : userPosts) {
                Long postId = post.getPid();
                System.out.println("삭제할 게시글 ID: " + postId);

                // 태그 삭제 전에 해당 태그들을 저장
                for (PostTagEntity postTag : post.getPostTags()) {
                    tagsToCheck.add(postTag.getTag());
                }

                // 특정 게시글의 태그 관계 삭제
                postTagRepository.deleteByPost(post);

                // 좋아요 정보 삭제
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

            // 6. 사용되지 않는 태그 삭제
            for (TagEntity tag : tagsToCheck) {
                if (postTagRepository.countByTag(tag) == 0) { // 다른 게시글에서 사용되지 않으면 삭제
                    tagRepository.delete(tag);
                }
            }

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