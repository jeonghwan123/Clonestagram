package com.goorm.clonestagram.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goorm.clonestagram.post.dto.PostInfoDto;
import lombok.*;
import org.springframework.data.domain.Page;


/**
 * 게시글 조회 응답 위한 DTO
 * - totalCount, postList를 반환
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPostResDto {
    private Long totalCount;

    @JsonProperty("content")
    private Page<PostInfoDto> postList;
}


