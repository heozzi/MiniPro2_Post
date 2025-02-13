package com.example.minipro2_post.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 댓글  DTO
 */
@Data
@ToString
public class CommentDto {
    private Long cid;
    private String content;
    private Long uid;
}
