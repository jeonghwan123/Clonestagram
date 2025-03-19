package com.goorm.clonestagram.file.controller;

import com.goorm.clonestagram.file.dto.ImageUpdateReqDto;
import com.goorm.clonestagram.file.dto.ImageUpdateResDto;
import com.goorm.clonestagram.file.dto.ImageUploadReqDto;
import com.goorm.clonestagram.file.dto.ImageUploadResDto;
import com.goorm.clonestagram.file.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 이미지 업로드 요청을 처리하는 컨트롤러
 * - 클라이언트로부터 이미지 업로드 요청을 받아 검증 및 서비스 호출을 수행
 */
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * 이미지 업로드
     * - 요청으로부터 파일을 받아 유효성 검사 후, 서비스 계층에 전달
     *
     * @param imageUploadReqDto 업로드할 이미지와 관련된 요청 DTO
     * @return 업로드 성공 시 ImageUploadResDto 반환
     * @throws Exception 업로드 도중 발생할 수 있는 예외
     */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//file 업로드를 위해 추가
    public ResponseEntity<ImageUploadResDto> imageUpload(/*LoginUser loginUser,*/ ImageUploadReqDto imageUploadReqDto) throws Exception {

        // 1. 업로드된 파일이 없거나 비어있는 경우 예외 처리
        if(imageUploadReqDto.getFile() == null || imageUploadReqDto.getFile().isEmpty()){
            throw new IllegalArgumentException("업로드할 파일이 없습니다");
        }

        // 2. 업로드된 파일의 Content-Type이 'image/'로 시작하지 않으면 이미지 파일 아님 → 예외 처리
        if(!imageUploadReqDto.getFile().getContentType().startsWith("image/")){
            throw new IllegalArgumentException("이미지를 업로드해 주세요");
        }

        // 3. 검증이 통과된 경우 서비스 계층 호출 및 응답 반환
        return ResponseEntity.ok(imageService.imageUpload(imageUploadReqDto/*, loginUser.getId()*/));
    }

    @PutMapping(value = "/image/{postSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUpdateResDto> imageUpdate(@PathVariable("postSeq") Long postSeq, ImageUpdateReqDto imageUpdateReqDto){

        if(imageUpdateReqDto.getFile() != null && !imageUpdateReqDto.getFile().getContentType().startsWith("image/")){
            throw new IllegalArgumentException("이미지를 업로드해 주세요");
        }

        return ResponseEntity.ok(imageService.imageUpdate(postSeq, imageUpdateReqDto));
    }
}
