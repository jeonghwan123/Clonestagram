package com.goorm.clonestagram.error;

import lombok.*;

/**
 * 에러 응답을 위한 DTO
 * - errorMessage를 반환
 */
@Getter
@Builder
public class ErrorResponseDto {
    private String errorMessage;
}
