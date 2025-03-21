package com.goorm.clonestagram.file.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지 수정 요청을 위한 DTO
 * - file, content를 클라이언트에게 받음
 */
@Getter
@Setter
public class ImageUpdateReqDto {

    @Schema(type = "string", format = "binary")//file 업로드를 위해 추가
    private MultipartFile file;
    private String content;

}
