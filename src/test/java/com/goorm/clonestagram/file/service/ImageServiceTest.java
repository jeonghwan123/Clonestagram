package com.goorm.clonestagram.file.service;

import com.goorm.clonestagram.file.ContentType;
import com.goorm.clonestagram.file.domain.Posts;
import com.goorm.clonestagram.file.dto.ImageUpdateReqDto;
import com.goorm.clonestagram.file.dto.ImageUpdateResDto;
import com.goorm.clonestagram.file.dto.ImageUploadReqDto;
import com.goorm.clonestagram.file.dto.ImageUploadResDto;
import com.goorm.clonestagram.file.repository.PostsRepository;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//Todo 로그인 구현 완료 후 유저를 포함하여 테스트 필요
class ImageServiceTest {

    private String uploadFolder = "src/main/resources/static/uploads/image";

    private ImageUploadReqDto imageUploadReqDto;

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        imageUploadReqDto = new ImageUploadReqDto();
        ReflectionTestUtils.setField(imageService, "uploadFolder", "uploads/image");
    }

    @Test
    public void 파일생성테스트(){

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "test-image.jpg","image/jpeg","dummy image content".getBytes()
        );

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" +mockMultipartFile.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadFolder + imageFileName);
        try{
            Files.createDirectories(Paths.get(uploadFolder));
            Files.write(imageFilePath, mockMultipartFile.getBytes());
            assertTrue(Files.exists(imageFilePath));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void 생성완료테스트() throws Exception{
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "test-image.jpg","image/jpeg","dummy image content".getBytes()
        );

        imageUploadReqDto.setFile(mockMultipartFile);
        imageUploadReqDto.setContent("테스트 내용");

        User testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .password("1234")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(postsRepository.save(any(Posts.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImageUploadResDto imageUploadResDto = imageService.imageUpload(imageUploadReqDto, testUser.getId());

        assertNotNull(imageUploadResDto);
        assertNotNull(imageUploadResDto.getCreatedAt());
        verify(userRepository).findByUsername(testUser.getUsername());
        verify(postsRepository).save(any(Posts.class));
    }

    @Test
    public void 파일수정테스트(){
        Long postSeq = 1L;

        MockMultipartFile oldFile = new MockMultipartFile(
                "file", "old-image.jpg","image/jpeg","dummy image content".getBytes()
        );

        User testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .password("1234")
                .build();

        Posts tempPost = Posts.builder()
                .id(1L)
                .content("수정전 내용")
                .mediaName(oldFile.getOriginalFilename())
                .contentType(ContentType.IMAGE)
                .createdAt(LocalDateTime.now())
                .user(testUser)
                .build();

        MockMultipartFile newFile = new MockMultipartFile(
                "file", "new-image.jpg","image/jpeg","dummy image content".getBytes()
        );

        ImageUpdateReqDto reqDto = new ImageUpdateReqDto();
        reqDto.setFile(newFile);
        reqDto.setContent("수정된 내용");

        when(postsRepository.findById(eq(1L))).thenReturn(Optional.of(tempPost));
        when(postsRepository.save(any(Posts.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImageUpdateResDto imageUpdateResDto = imageService.imageUpdate(postSeq, reqDto, testUser.getId());

        assertNotNull(imageUpdateResDto);
        assertEquals("수정된 내용", imageUpdateResDto.getContent());
        assertNotNull(imageUpdateResDto.getUpdatedAt());
        verify(postsRepository).findById(postSeq);
        verify(postsRepository).save(any(Posts.class));

    }
}

