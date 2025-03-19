package com.goorm.clonestagram.domain;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 게시글 ID
    private Long userId;  // 게시글 작성자 ID
    private String content;  // 게시글 내용

    public PostEntity(long l, long l1, String testPost) {
        this.id = l;
        this.userId = l1;
        this.content = testPost;
    }
}
