package com.example.minipro2_post.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostDto {
    public Integer pid;
    public String uid;

    public String type;

    public Integer gid;
    public String content;

    public Integer cid;
    public Integer ulike;
    public String tag;

}
