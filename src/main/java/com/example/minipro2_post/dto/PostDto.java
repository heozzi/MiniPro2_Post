package com.example.minipro2_post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long pid;
    private Long uid;
    private String type;
    private Long gid;
    private LocalDateTime date;
    private String content;
    private Long cid;
    private Long like;
    private String tag;
    private String image;

    @Builder
    public PostDto
            (Long pid, Long uid, String type, Long gid,
             LocalDateTime date, String content, Long cid, Long like, String tag, String image) {
        this.pid = pid;
        this.uid = uid;
        this.type = type;
        this.gid = gid;
        this.date = date;
        this.content = content;
        this.cid = cid;
        this.like = like;
        this.tag = tag;
        this.image = image;
    }
}