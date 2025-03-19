package com.goorm.clonestagram.file.dto;

import com.goorm.clonestagram.file.ContentType;
import com.goorm.clonestagram.file.domain.Posts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * 이미지 수정 요청을 위한 DTO
 * - file, content, type을 클라이언트에게 받음
 * - updatedAt 생성
 */
@Getter
@Setter
public class ImageUpdateReqDto {

    @Schema(type = "string", format = "binary")//file 업로드를 위해 추가
    private MultipartFile file;
    private String content;

}
