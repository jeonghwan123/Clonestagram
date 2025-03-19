package com.goorm.clonestagram.file.service;

import com.goorm.clonestagram.file.domain.Posts;
import com.goorm.clonestagram.file.dto.*;
import com.goorm.clonestagram.file.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final PostsRepository postsRepository;

    @Value("${video.path}")
    private String uploadFolder;

    public VideoUploadResDto videoUpload(VideoUploadReqDto videoUploadReqDto) {
        //1. unique 파일명을 생성하기 위해 uuid 사용
        UUID uuid = UUID.randomUUID();
        String videoFileName = uuid + "_" + videoUploadReqDto.getFile().getOriginalFilename();
        //2. unique 파일명과 upload 위치를 활용해 파일 경로 생성
        Path videoFilePath = Paths.get(uploadFolder).resolve(videoFileName);

        try{
            //3. 파일 경로를 활용해 디렉토리를 생성하고 파일을 저장
            Files.createDirectories(Paths.get(uploadFolder));
            Files.write(videoFilePath, videoUploadReqDto.getFile().getBytes());

        }catch (IOException e){
            throw new RuntimeException("파일 저장 중 오류 발생 : " + e.getMessage());
        }

        //4. unique 파일명과 videoUploadReqDto의 값들을 활용해 Entity 객체 생성 후 저장
        Posts postEntity = videoUploadReqDto.toEntity(videoFileName);
        Posts post = postsRepository.save(postEntity);

        //5. 모든 작업이 완료된 경우 응답 반환
        return VideoUploadResDto.builder()
                .content(post.getContent())
                .type(post.getContentType())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public VideoUpdateResDto videoUpdate(Long postSeq, VideoUpdateReqDto videoUpdateReqDto) {

        boolean updated = false;
        //1. 게시글 ID를 통해 게시글을 찾아 반환
        Posts posts = postsRepository.findById(postSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다"));

        //2. 이미지 수정 여부 파악
        if(videoUpdateReqDto.getFile() != null && !videoUpdateReqDto.getFile().isEmpty()){

            //2-1. unique 파일명을 생성하기 위해 uuid 사용
            UUID uuid = UUID.randomUUID();
            String videoFileName = uuid + "_" + videoUpdateReqDto.getFile().getOriginalFilename();

            //2-2. unique 파일명과 upload 위치를 활용해 파일 경로 생성
            Path videoFilePath = Paths.get(uploadFolder).resolve(videoFileName);

            try{
                //2-3. 파일 경로를 활용해 디렉토리를 생성하고 파일을 저장
                Files.write(videoFilePath, videoUpdateReqDto.getFile().getBytes());
            }catch (IOException e){
                throw new RuntimeException("파일 저장 중 오류 발생 : " + e.getMessage());
            }
            //2-4. 수정된 파일명 반영
            String oldFileName = posts.getMediaName();
            posts.setMediaName(videoFileName);

            //2-5. 기존의 파일 삭제
            Path oldVideoPath = Paths.get(uploadFolder + oldFileName);
            try{
                Files.deleteIfExists(oldVideoPath);
            } catch (IOException e){
                throw new RuntimeException("파일 삭제 중 오류 발생 : " + e.getMessage());
            }

            //2-6. 업데이트 되었음을 표시
            updated = true;
        }

        //3. 게시글 내용 수정 여부 파악
        if(videoUpdateReqDto.getContent() != null && !videoUpdateReqDto.getContent().trim().isEmpty()){
            //3-1. 수정된 게시글 내용 반영
            posts.setContent(videoUpdateReqDto.getContent());

            //3-2. 업데이트 되었음을 표시
            updated = true;
        }

        Posts updatedPost;
        if(updated){
            //4. 업데이트 된 경우 업데이트일시 반영
            posts.setUpdatedAt(LocalDateTime.now());

            //5. 업데이트된 게시글을 DB에 저장
            updatedPost = postsRepository.save(posts);
        }else{
            updatedPost = posts;
        }


        //6. 모든 작업이 완료된 경우 응답 반환
        return VideoUpdateResDto.builder()
                .content(updatedPost.getContent())
                .type(updatedPost.getContentType())
                .updatedAt(updatedPost.getUpdatedAt())
                .build();
    }

    /**
     *
     * @param postSeq 삭제할 게시글 식별자
     */
    public void videoDelete(Long postSeq) {
        //1. 식별자를 토대로 게시글 찾아 반환
        Posts posts = postsRepository.findById(postSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));

        //2. 기존에 존재하던 파일 삭제
        Path filePath = Paths.get(uploadFolder).resolve(posts.getMediaName());
        try{
            Files.deleteIfExists(filePath);
        } catch (IOException e){
            throw new RuntimeException("파일 삭제 중 오류 발생 : " + e.getMessage());
        }

        //3. DB에서 데이터 삭제
        postsRepository.delete(posts);
    }
}
