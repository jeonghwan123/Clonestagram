package com.goorm.clonestagram.user.service;

import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.dto.UserProfileDto;
import com.goorm.clonestagram.user.dto.UserProfileUpdateDto;
import com.goorm.clonestagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ProfileServiceIntegrationTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // 테스트용 사용자 생성
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password(passwordEncoder.encode("oldpassword"))
                .bio("Test bio")
                .profileimg("old-profile.jpg")
                .build();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("사용자 프로필 조회 테스트")
    public void testGetUserProfile() {
        // When
        UserProfileDto profileDto = profileService.getUserProfile(testUser.getId());

        // Then
        assertThat(profileDto).isNotNull();
        assertThat(profileDto.getUsername()).isEqualTo("testuser");
        assertThat(profileDto.getEmail()).isEqualTo("test@example.com");
        assertThat(profileDto.getBio()).isEqualTo("Test bio");
    }

    @Test
    @DisplayName("사용자 프로필 업데이트 테스트")
    public void testUpdateUserProfile() {
        // Given
        UserProfileUpdateDto updateDto = UserProfileUpdateDto.builder()
                .username("updateduser")
                .email("updated@example.com")
                .password("newpassword")
                .bio("Updated bio")
                .profileImage("new-profile.jpg")
                .build();

        // When
        UserProfileDto updatedProfile = profileService.updateUserProfile(testUser.getId(), updateDto);

        // Then
        assertThat(updatedProfile.getUsername()).isEqualTo("updateduser");
        assertThat(updatedProfile.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedProfile.getBio()).isEqualTo("Updated bio");

        // 실제 DB에서 확인
        User updatedUser = userRepository.findByIdAndDeletedIsFalse(testUser.getId())
                .orElseThrow();

        // 비밀번호 검증 (암호화되었는지)
        assertThat(passwordEncoder.matches("newpassword", updatedUser.getPassword())).isTrue();
        assertThat(updatedUser.getProfileimg()).isEqualTo("new-profile.jpg");
    }



    @Test
    @DisplayName("존재하지 않는 사용자 프로필 조회 시 예외 발생")
    public void testGetNonExistentUserProfile() {
        // When & Then
        assertThatThrownBy(() -> profileService.getUserProfile(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }
}