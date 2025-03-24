package com.goorm.clonestagram.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentRequest {
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
}
