package com.goorm.clonestagram.file.dto;

import com.goorm.clonestagram.file.ContentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class VideoUpdateResDto {
    //    private User user;
    private String content;
    private ContentType type;
    private LocalDateTime updatedAt;
}

