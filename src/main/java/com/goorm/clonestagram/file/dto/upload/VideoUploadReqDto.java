package com.goorm.clonestagram.file.dto.upload;

import com.goorm.clonestagram.file.ContentType;
import com.goorm.clonestagram.file.domain.Posts;
import com.goorm.clonestagram.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지 업로드 요청을 위한 DTO
 * - file, content, type을 클라이언트에게 받음
 * - createdAt을 생성
 */
@Getter
@Setter
public class VideoUploadReqDto {

    @Schema(type = "string", format = "binary")//file 업로드를 위해 추가
    private MultipartFile file;
    private String content;

    public Posts toEntity(String imageName , User user) {
        return Posts.builder()
                .user(user)
                .content(content)
                .mediaName(imageName)
                .contentType(ContentType.VIDEO)
                .build();
    }
}
