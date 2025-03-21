package com.goorm.clonestagram.file.controller;

import com.goorm.clonestagram.file.dto.update.ImageUpdateReqDto;
import com.goorm.clonestagram.file.dto.update.ImageUpdateResDto;
import com.goorm.clonestagram.file.dto.upload.ImageUploadReqDto;
import com.goorm.clonestagram.file.dto.upload.ImageUploadResDto;
import com.goorm.clonestagram.file.service.ImageService;
import com.goorm.clonestagram.util.TempUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    //Todo TempUserDetail 변경
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//file 업로드를 위해 추가
    public ResponseEntity<ImageUploadResDto> imageUpload(@AuthenticationPrincipal TempUserDetail userDetail, ImageUploadReqDto imageUploadReqDto) throws Exception {

        Long userId = userDetail.getId();

        // 1. 업로드된 파일이 없거나 비어있는 경우 예외 처리
        if(imageUploadReqDto.getFile() == null || imageUploadReqDto.getFile().isEmpty()){
            throw new IllegalArgumentException("업로드할 파일이 없습니다");
        }

        // 2. 업로드된 파일의 Content-Type이 'image/'로 시작하지 않으면 이미지 파일 아님 → 예외 처리
        if(!imageUploadReqDto.getFile().getContentType().toLowerCase().startsWith("image/")){
            throw new IllegalArgumentException("이미지를 업로드해 주세요");
        }

        // 3. 검증이 통과된 경우 서비스 계층 호출 및 응답 반환
        return ResponseEntity.ok(imageService.imageUpload(imageUploadReqDto, userId));
    }

    /**
     * 이미지 수정
     * - 요청으로 부터 파일, 게시글 내용을 받아 유효성 검사 후 서비스 계층에 넘김
     * - 가능한 수정 방식 : 파일만 수정, 내용만 수정, 둘다 수정, 둘다 수정 안함
     *
     * @param postSeq 게시글의 고유 번호
     * @param imageUpdateReqDto
     * @return
     */
    //Todo TempUserDetail 변경
    @PutMapping(value = "/image/{postSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUpdateResDto> imageUpdate(@PathVariable("postSeq") Long postSeq,
                                                         @AuthenticationPrincipal TempUserDetail userDetail,
                                                         ImageUpdateReqDto imageUpdateReqDto){
        Long userId = userDetail.getId();

        if(imageUpdateReqDto.getFile() != null && !imageUpdateReqDto.getFile().getContentType().toLowerCase().startsWith("image/")){
            throw new IllegalArgumentException("이미지를 업로드해 주세요");
        }

        return ResponseEntity.ok(imageService.imageUpdate(postSeq, imageUpdateReqDto, userId));
    }

    /**
     * 이미지 삭제
     * - 삭제를 원하는 게시글의 식별자를 받아 서비스 계층에 넘김
     *
     * @param postSeq 삭제할 게시글 식별자
     * @return ResponseEntity
     */
    //Todo TempUserDetail 변경
    @DeleteMapping("/image/{postSeq}")
    public ResponseEntity<?> imageDelete(@PathVariable Long postSeq, @AuthenticationPrincipal TempUserDetail userDetail){

        Long userId = userDetail.getId();

        imageService.imageDelete(postSeq, userId);

        return ResponseEntity.ok("삭제 완료");
    }
}
