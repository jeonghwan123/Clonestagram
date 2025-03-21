package com.goorm.clonestagram.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class LikeDto {

    private Long userId;
    private Long postId;
    private Long likeCount;
    private LocalDateTime createdAt;

}
