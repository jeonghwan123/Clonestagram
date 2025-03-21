package com.goorm.clonestagram.file.dto;

import com.goorm.clonestagram.user.dto.UserProfileDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

/**
 * 피드 요청 응답을 위한 DTO
 * - user정보와 페이징 처리된 feed 리스트를 반환
 */
@Getter
@Builder
public class FeedResDto {
    private UserProfileDto user;
    private Page<PostInfoDto> feed;
}
