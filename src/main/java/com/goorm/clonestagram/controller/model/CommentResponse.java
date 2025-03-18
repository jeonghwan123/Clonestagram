package com.goorm.clonestagram.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private long id;
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
}
