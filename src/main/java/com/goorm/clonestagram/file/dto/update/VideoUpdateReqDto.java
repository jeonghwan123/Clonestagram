package com.goorm.clonestagram.file.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VideoUpdateReqDto {
    @Schema(type = "string", format = "binary")//file 업로드를 위해 추가
    private MultipartFile file;
    private String content;
}
