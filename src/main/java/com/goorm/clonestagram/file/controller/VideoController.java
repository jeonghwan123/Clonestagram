package com.goorm.clonestagram.file.controller;


import com.goorm.clonestagram.file.dto.VideoUpdateReqDto;
import com.goorm.clonestagram.file.dto.VideoUpdateResDto;
import com.goorm.clonestagram.file.dto.VideoUploadReqDto;
import com.goorm.clonestagram.file.dto.VideoUploadResDto;
import com.goorm.clonestagram.file.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping(value = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//file 업로드를 위해 추가
    public ResponseEntity<VideoUploadResDto> videoUpload(/*LoginUser loginUser,*/ VideoUploadReqDto videoUploadReqDto) throws Exception {

        // 1. 업로드된 파일이 없거나 비어있는 경우 예외 처리
        if(videoUploadReqDto.getFile() == null || videoUploadReqDto.getFile().isEmpty()){
            throw new IllegalArgumentException("업로드할 파일이 없습니다");
        }

        // 2. 업로드된 파일의 Content-Type이 'video/'로 시작하지 않으면 이미지 파일 아님 → 예외 처리
        if(!videoUploadReqDto.getFile().getContentType().startsWith("video/")){
            throw new IllegalArgumentException("영상을 업로드해 주세요");
        }

        // 3. 검증이 통과된 경우 서비스 계층 호출 및 응답 반환
        return ResponseEntity.ok(videoService.videoUpload(videoUploadReqDto/*, loginUser.getId()*/));
    }
    /**
     * 이미지 수정
     * - 요청으로 부터 파일, 게시글 내용을 받아 유효성 검사 후 서비스 계층에 넘김
     * - 가능한 수정 방식 : 파일만 수정, 내용만 수정, 둘다 수정, 둘다 수정 안함
     *
     * @param postSeq 게시글의 고유 번호
     * @param videoUpdateReqDto
     * @return
     */
    @PutMapping(value = "/video/{postSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoUpdateResDto> videoUpdate(@PathVariable("postSeq") Long postSeq, VideoUpdateReqDto videoUpdateReqDto){

        if(videoUpdateReqDto.getFile() != null && !videoUpdateReqDto.getFile().getContentType().startsWith("video/")){
            throw new IllegalArgumentException("이미지를 업로드해 주세요");
        }

        return ResponseEntity.ok(videoService.videoUpdate(postSeq, videoUpdateReqDto));
    }

    /**
     * 이미지 삭제
     * - 삭제를 원하는 게시글의 식별자를 받아 서비스 계층에 넘김
     *
     * @param postSeq 삭제할 게시글 식별자
     * @return ResponseEntity
     */
    @DeleteMapping("/video/{postSeq}")
    public ResponseEntity<?> videoDelete(@PathVariable Long postSeq){

        videoService.videoDelete(postSeq);

        return ResponseEntity.ok("삭제 완료");
    }
}
