package com.example.minipro2_post.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "post")
@ToString
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;                   // 게시글 번호

    private Long uid;                   // 작성자 번호
    private String type;                // 게시글 타입
    private Long gid;                   // 그룹 번호
    private LocalDateTime date;         // 작성 날짜
    private String content;             // 게시글 내용

    @JsonManagedReference   // 직렬화 가능하도록 설정
    @OneToMany(mappedBy = "pid",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("cid asc")                 // 댓글 번호 순서대로 정렬
    private List<CommentEntity> comments;    // 댓글 리스트
    private Long youLike;               // 좋아요 개수
    private String image;               // 이미지 url

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PostTagEntity> postTags = new HashSet<>();


    @Builder
    public PostEntity(Long pid, Long uid, String type, Long gid, LocalDateTime date, String content, Long youLike, String image, Set<PostTagEntity> postTags) {
        this.pid = pid;
        this.uid = uid;
        this.type = type;
        this.gid = gid;
        this.date = date;
        this.content = content;
        this.youLike = youLike;
        this.image = image;
        this.postTags = postTags != null ? postTags : new HashSet<>();
    }
}
