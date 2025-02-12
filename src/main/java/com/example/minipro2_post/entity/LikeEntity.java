package com.example.minipro2_post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "likes")
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lid;            // 좋아요 번호

    private Long pid;           // 게시글 번호
    private Long uid;           // 좋아요 누른 유저 번호

    @Builder
    public LikeEntity(Long lid, Long pid, Long uid) {
        this.lid = lid;
        this.pid = pid;
        this.uid = uid;
    }
}
