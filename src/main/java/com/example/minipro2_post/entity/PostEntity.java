package com.example.minipro2_post.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name="post")
@NoArgsConstructor
@ToString
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer pid;

    public Integer uid;
    public String type;

    public Integer gid;

    public String content;

    public Integer cid;
    public Integer ulike;
    public String tag;
}
