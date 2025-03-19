package com.goorm.clonestagram.file.service;

import com.goorm.clonestagram.file.domain.user.User;
import com.goorm.clonestagram.file.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // 테스트 실행 전 기존 데이터 삭제
    }

    @Test
    public void updateUserProfile_Success() {
        // Given
        User user = User.builder()
                .username("testuser")  // 필수 필드 추가
                .password("password123")  // 필수 필드 추가
                .email("testuser@example.com")  // 필수 필드 추가
                .profileimg("http://example.com/profile.jpg")
                .bio("안녕하세요. 테스트 사용자입니다.")
                .build();

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("testuser@example.com", savedUser.getEmail());
        assertEquals("http://example.com/profile.jpg", savedUser.getProfileimg());
        assertEquals("안녕하세요. 테스트 사용자입니다.", savedUser.getBio());
    }

    @Test
    public void getUserProfile_Success() {
        // Given
        User user = User.builder()
                .username("testuser2")
                .password("password456")
                .email("testuser2@example.com")
                .profileimg("http://example.com/profile2.jpg")
                .bio("두 번째 사용자입니다.")
                .build();
        userRepository.save(user);

        // When
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        // Then
        assertNotNull(foundUser);
        assertEquals("testuser2", foundUser.getUsername());
        assertEquals("testuser2@example.com", foundUser.getEmail());
        assertEquals("http://example.com/profile2.jpg", foundUser.getProfileimg());
        assertEquals("두 번째 사용자입니다.", foundUser.getBio());
    }

    @Test
    public void updateProfileImage_Success() {
        // Given
        User user = User.builder()
                .username("testuser3")
                .password("password789")
                .email("testuser3@example.com")
                .profileimg("http://example.com/profile3.jpg")
                .bio("세 번째 사용자입니다.")
                .build();
        User savedUser = userRepository.save(user);

        // When
        savedUser.setUsername("testuser4");
        savedUser.setEmail("testuser4@example.com");
        savedUser.setBio("update!");
        savedUser.setProfileimg("http://example.com/newprofile3.jpg");
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertEquals("http://example.com/newprofile3.jpg", updatedUser.getProfileimg());
        assertEquals("update!", updatedUser.getBio());
        assertEquals("testuser4", updatedUser.getUsername());
        assertEquals("testuser4@example.com", updatedUser.getEmail());
    }
}