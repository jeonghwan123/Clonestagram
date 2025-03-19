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

/**
 * 양상 업로드 요청을 처리하는 컨트롤러
 * - 클라이언트로부터 영상 업로드 요청을 받아 검증 및 서비스 호출을 수행
 */
@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /**
     * 영상 업로드
     * - 요청으로부터 파일을 받아 유효성 검사 후, 서비스 계층에 전달
     *
     * @param videoUploadReqDto 업로드할 영상과 관련된 요청 DTO
     * @return 업로드 성공 시 VideoUploadResDto 반환
     * @throws Exception 업로드 도중 발생할 수 있는 예외
     */
    @PostMapping(value = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//file 업로드를 위해 추가
    public ResponseEntity<VideoUploadResDto> videoUpload(/*LoginUser loginUser,*/ VideoUploadReqDto videoUploadReqDto) throws Exception {

        // 1. 업로드된 파일이 없거나 비어있는 경우 예외 처리
        if(videoUploadReqDto.getFile() == null || videoUploadReqDto.getFile().isEmpty()){
            throw new IllegalArgumentException("업로드할 파일이 없습니다");
        }

        // 2. 업로드된 파일의 Content-Type이 'video/'로 시작하지 않으면 영상 파일 아님 → 예외 처리
        if(!videoUploadReqDto.getFile().getContentType().startsWith("video/")){
            throw new IllegalArgumentException("영상을 업로드해 주세요");
        }

        // 3. 검증이 통과된 경우 서비스 계층 호출 및 응답 반환
        return ResponseEntity.ok(videoService.videoUpload(videoUploadReqDto/*, loginUser.getId()*/));
    }
    /**
     * 영상 수정
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
            throw new IllegalArgumentException("영상을 업로드해 주세요");
        }

        return ResponseEntity.ok(videoService.videoUpdate(postSeq, videoUpdateReqDto));
    }

    /**
     * 영상 삭제
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
