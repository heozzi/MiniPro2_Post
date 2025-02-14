package com.example.minipro2_post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 댓글  DTO
 */
@Data
@ToString
public class CommentDto {
//    private Long cid;
    private Long uid;
    private String content;


    @Builder
    public CommentDto(Long uid, String content) {
        this.uid = uid;
        this.content = content;
    }
}
