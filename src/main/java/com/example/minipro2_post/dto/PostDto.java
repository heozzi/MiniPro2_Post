package com.example.minipro2_post.dto;

import com.example.minipro2_post.entity.PostEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
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

    public PostDto(PostEntity post) {
        this.pid = post.getPid();
        this.uid = post.getUid();
        this.type = post.getType();
        this.gid = post.getGid();
        this.date = post.getDate();
        this.content = post.getContent();
        this.like = post.getYouLike();
        this.image = post.getImage();
        this.commentCount = (post.getComments() != null) ? post.getComments().size() : 0;

        // PostTagEntity의 ID 대신 TagEntity의 tagName만 저장
        this.tags = post.getPostTags().stream()
                .map(postTag -> postTag.getTag().getTagName()) // tagName만 추출
                .collect(Collectors.toList());
    }
}