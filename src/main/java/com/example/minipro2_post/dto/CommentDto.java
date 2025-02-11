package com.example.minipro2_post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 댓글  DTO
 */
@Data
@ToString
public class CommentDto {
    private Integer cid;
    private String content;
}
