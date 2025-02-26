package com.example.minipro2_post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {
    private Long pid;
    private Long uid;
    private String type;
    private Long gid;
    private LocalDateTime date;
    private String content;
    private Long like;
    private List<String> tags;
    private String image;
    private Integer commentCount;   // 댓글 개수

    @Builder
    public PostDto
            (Long pid, Long uid, String type, Long gid,
             LocalDateTime date, String content, Long like, List<String> tags, String image, Integer commentCount) {
        this.pid = pid;
        this.uid = uid;
        this.type = type;
        this.gid = gid;
        this.date = date;
        this.content = content;
        this.like = like;
        this.tags = tags;
        this.image = image;
        this.commentCount = commentCount;
    }
}