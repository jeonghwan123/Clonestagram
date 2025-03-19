package com.goorm.clonestagram.file.service;

import com.goorm.clonestagram.file.domain.Posts;
import com.goorm.clonestagram.file.dto.ImageUploadReqDto;
import com.goorm.clonestagram.file.dto.ImageUploadResDto;
import com.goorm.clonestagram.file.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 이미지 업로드 요청을 처리하는 서비스
 * - 검증이 완료된 이미지를 받아 업로드 서비스 수행
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostsRepository postsRepository;

    @Value("${image.path}")
    private String uploadFolder;

    /**
     * 이미지 업로드
     * - 검증이 끝난 파일의 name과 path를 설정하여 DB와 저장소에 저장
     *
     * @param imageUploadReqDto 업로드할 이미지와 관련된 요청 DTO
     *          - controller에서 넘어옴
     * @return 성공시 ImageUploadResDto 반환
     * @throws Exception
     *          - 파일 저장시 IOException 발생
     */
    public ImageUploadResDto imageUpload(ImageUploadReqDto imageUploadReqDto/*, long userId*/) throws Exception {
//        User userEntity = userRepository.findByid(userId).orElseThrow(null);

        //1. unique 파일명을 생성하기 위해 uuid 사용
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + imageUploadReqDto.getFile().getOriginalFilename();

        //2. unique 파일명과 upload 위치를 활용해 파일 경로 생성
        Path imageFilePath = Paths.get(uploadFolder).resolve(imageFileName);

        try{
            //3. 파일 경로를 활용해 디렉토리를 생성하고 파일을 저장
            Files.createDirectories(Paths.get(uploadFolder));
            Files.write(imageFilePath, imageUploadReqDto.getFile().getBytes());

        }catch (IOException e){
            throw new RuntimeException("파일 저장 중 오류 발생 : " + e.getMessage());
        }

        //4. unique 파일명과 imageUploadReqDto의 값들을 활용해 Entity 객체 생성 후 저장
        Posts postEntity = imageUploadReqDto.toEntity(imageFileName);
        Posts post = postsRepository.save(postEntity);

        //5. 모든 작업이 완료된 경우 응답 반환
        return ImageUploadResDto.builder()
                .content(post.getContent())
                .type(post.getContentType())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
