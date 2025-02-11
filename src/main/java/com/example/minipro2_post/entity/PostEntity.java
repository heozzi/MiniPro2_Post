package com.example.minipro2_post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "post")
@ToString
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;               // 게시글 번호

    private Long uid;               // 작성자 번호
    private String type;            // 게시글 타입
    private Long gid;               // 그룹 번호
    private LocalDateTime date;     // 작성 날짜
    private String content;         // 게시글 내용
    private Long cid;               // 댓글 번호
    private Long youLike;           // 좋아요 개수
    private String tag;             // 태그
    private String image;           // 이미지 url

    @Builder
    public PostEntity(Long pid, Long uid, String type, Long gid, LocalDateTime date, String content, Long cid, Long youLike, String tag, String image) {
        this.pid = pid;
        this.uid = uid;
        this.type = type;
        this.gid = gid;
        this.date = date;
        this.content = content;
        this.cid = cid;
        this.youLike = youLike;
        this.tag = tag;
        this.image = image;
    }
}
