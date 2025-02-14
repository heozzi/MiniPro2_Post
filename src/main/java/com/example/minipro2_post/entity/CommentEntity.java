package com.example.minipro2_post.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name="Comment")
@NoArgsConstructor
@ToString(exclude = "pid") // pid는 PostEntity와 양방향 관계이므로 제외
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long cid;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="P_id",nullable = false)
    private PostEntity pid;

    @JoinColumn(referencedColumnName = "User", name="U_id",nullable = false)
    private Long uid;

    private String content;

    @Builder
    public CommentEntity(Long cid, PostEntity pid, Long uid, String content) {
        this.cid = cid;
        this.pid = pid;
        this.uid = uid;
        this.content = content;
    }

}
