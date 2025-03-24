package com.goorm.clonestagram.search.dto;

import com.goorm.clonestagram.post.dto.PostInfoDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;


/**
 * 게시글 조회 응답 위한 DTO
 * - totalCount, postList를 반환
 */
@Getter
@Builder
public class SearchPostResDto {
    private Long totalCount;
    private Page<PostInfoDto> postList;
}


