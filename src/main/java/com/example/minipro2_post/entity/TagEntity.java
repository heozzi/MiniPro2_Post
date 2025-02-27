package com.example.minipro2_post.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tag")
@ToString(exclude = "postTags")
@EqualsAndHashCode(exclude = "postTags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;       // 태그 ID

    @Column(unique = true, nullable = false)
    private String tagName;    // 태그 이름

    @JsonManagedReference
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PostTagEntity> postTags;

    @Builder
    public TagEntity(String tagName) {
        this.tagName = tagName;
    }
}
