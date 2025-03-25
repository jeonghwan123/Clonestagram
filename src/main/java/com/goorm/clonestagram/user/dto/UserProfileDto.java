package com.goorm.clonestagram.user.dto;

import com.goorm.clonestagram.file.dto.PostInfoDto;
import com.goorm.clonestagram.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 프로필 조회를 위한 DTO
 * 프로필에 필요한 개인정보를 모두 반환
 */

@Getter
@Builder
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private String bio;
    private String profileimg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int followerCount;
    private int followingCount;

    private List<PostInfoDto> posts;


    public static UserProfileDto fromEntity(User user) {
        return UserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profileimg(user.getProfileimg())
                .build();
    }
}
