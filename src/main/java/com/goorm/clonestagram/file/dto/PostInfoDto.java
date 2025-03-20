package com.goorm.clonestagram.file.dto;

import com.goorm.clonestagram.file.ContentType;
import com.goorm.clonestagram.file.domain.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostInfoDto {
    private Long id;
    private String content;
    private String mediaName;
    private ContentType contentType;
    private LocalDateTime createdAt;

    public static PostInfoDto fromEntity(Posts post) {
        return PostInfoDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .mediaName(post.getMediaName())
                .contentType(post.getContentType())
                .createdAt(post.getCreatedAt())
                .build();
    }
}

