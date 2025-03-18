package com.goorm.clonestagram.controller.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentRequest {
    private String id;
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
}
