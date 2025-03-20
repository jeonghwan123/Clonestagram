package com.goorm.clonestagram.file.dto;

import com.goorm.clonestagram.user.dto.UserProfileDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;


@Getter
@Builder
public class FeedResDto {
    private UserProfileDto user;
    private Page<PostInfoDto> feed;
}
