package com.goorm.clonestagram.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 프로필 조회를 위한 DTO
 * 프로필에 필요한 개인정보를 모두 반환
 */

@Getter
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private String bio;
    private String profileimg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
