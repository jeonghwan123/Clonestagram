package com.goorm.clonestagram.follow.dto;

import com.goorm.clonestagram.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
public class FollowDto {
    private Long id;               // 팔로우 관계 ID
    private Long fromUserId;       // 팔로우 하는 유저 ID
    private Long toUserId;         // 팔로우 받는 유저 ID
    private String fromUsername;   // 팔로우 하는 유저 이름
    private String toUsername;     // 팔로우 받는 유저 이름
    private String fromProfileimg; // 팔로우 하는 유저 프로필 이미지
    private String toProfileImg;   // 팔로우 받는 유저 프로필 이미지
    private LocalDateTime createdAt; // 팔로우 생성 시간

    public FollowDto(Long id, User fromUser, User toUser, LocalDateTime createdAt, String fromUsername, String toUsername, String fromProfileImg, String toProfileImg) {
        this.id = id;
        this.fromUserId = fromUser.getId();
        this.toUserId = toUser.getId();
        this.createdAt = createdAt;
        this.fromUsername = fromUser.getUsername(); // 팔로우 하는 유저 이름
        this.toUsername = toUser.getUsername();     // 팔로우 받는 유저 이름
        this.fromProfileimg = fromUser.getProfileimg(); // 팔로우 하는 유저 프로필 이미지
        this.toProfileImg = toUser.getProfileimg();   // 팔로우 받는 유저 프로필 이미지
    }

}
