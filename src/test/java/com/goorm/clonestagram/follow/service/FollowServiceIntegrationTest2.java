package com.goorm.clonestagram.follow.service;

import com.goorm.clonestagram.follow.domain.Follows;
import com.goorm.clonestagram.follow.repository.FollowRepository;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class FollowServiceIntegrationTest2 {
    private static final Logger logger = LoggerFactory.getLogger(FollowServiceIntegrationTest2.class);

    @Autowired
    private FollowService followService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @BeforeEach
    public void setUp() {
        // 기존 데이터 모두 삭제
        followRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testMultiUserFollow() {
        int[] userCounts = {5, 10, 50, 100};

        for (int userCount : userCounts) {
            logger.info("===== 테스트 시작: 사용자 {} 명 =====", userCount);

            // 1. 타겟 유저를 먼저 명시적으로 저장
            User targetUser = createAndSaveTestUser("targetUser" + userCount, "target" + userCount + "@example.com");
            userRepository.flush(); // 변경사항 즉시 반영

            // 2. 사용자들 생성 및 저장 시 즉시 영속화
            List<User> users = new ArrayList<>();
            for (int i = 0; i < userCount; i++) {
                String email = "user" + userCount + "_" + i + "@example.com";
                User user = createAndSaveTestUser("user" + userCount + "_" + i, email);
                users.add(user);
            }
            userRepository.flush(); // 모든 사용자 즉시 저장

            // 정확한 수의 팔로워 생성
            int followersCount = (int) Math.round(userCount * 0.7);
            Set<Long> followerIds = new HashSet<>();

            // 3. 팔로우 처리 시 미리 저장된 ID 사용
            for (User user : users) {
                if (followerIds.size() < followersCount && !followerIds.contains(user.getId())) {
                    logger.info("사용자 {} ({})가 팔로우를 시도 중", user.getUsername(), user.getId());

                    // 이미 팔로우 상태인지 확인
                    boolean alreadyFollowing = followRepository.findByFromUserAndToUser(user, targetUser).isPresent();

                    if (!alreadyFollowing) {
                        // 명시적으로 저장된 사용자 ID 사용
                        followService.toggleFollow(user.getId(), targetUser.getId());
                        followerIds.add(user.getId());
                    }

                    if (followerIds.size() >= followersCount) {
                        break;
                    }
                }
            }

            // 4. 팔로우 상태 검증
            long actualFollowerCount = followRepository.getFollowerCount(targetUser.getId());
            logger.info("사용자 수: {}, 예상 팔로워 수: {}, 실제 팔로워 수: {}", userCount, followersCount, actualFollowerCount);

            // 정확한 팔로워 수 일치 검증
            assertEquals(followersCount, actualFollowerCount,
                    "팔로워 개수가 정확히 일치해야 합니다. (사용자 수: " + userCount + ")");

            // 5. 중복 팔로우 방지 검증
            List<Long> allFollowerIds = followRepository.findFollowerIdsByToUserId(targetUser.getId());
            Map<Long, Long> idCounts = new HashMap<>();
            for (Long id : allFollowerIds) {
                idCounts.put(id, idCounts.getOrDefault(id, 0L) + 1);
            }

            // 중복된 팔로우를 가진 ID 출력
            idCounts.forEach((id, count) -> {
                if (count > 1) {
                    logger.warn("중복된 팔로우 발견: 사용자 ID {} (팔로우 횟수: {})", id, count);
                }
            });

            // 중복된 팔로우가 없어야 함
            Set<Long> uniqueFollowerIds = new HashSet<>(allFollowerIds);
            assertEquals(followersCount, uniqueFollowerIds.size(), "중복된 팔로우가 없어야 합니다.");

            // 각 테스트 반복 후 데이터 초기화
            followRepository.deleteAll();
            userRepository.deleteAll();

            logger.info("===== 테스트 종료: 사용자 {} 명 =====\n", userCount);
        }
    }

    // 헬퍼 메서드 수정 - 생성과 동시에 저장
    private User createAndSaveTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setProfileimg("profileimg");
        return userRepository.saveAndFlush(user); // saveAndFlush 사용
    }
}