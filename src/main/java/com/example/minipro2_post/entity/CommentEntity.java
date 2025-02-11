package com.example.minipro2_post.entity;

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
@ToString
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer cid;

    @JoinColumn(referencedColumnName = "Post", name="P_id",nullable = false)
    private Integer pid;

    @JoinColumn(referencedColumnName = "User", name="U_id",nullable = false)
    private Integer uid;

    private String content;

    @Builder
    public CommentEntity(Integer cid, Integer pid, Integer uid, String content) {
        this.cid = cid;
        this.pid = pid;
        this.uid = uid;
        this.content = content;
    }

}
