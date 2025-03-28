package com.goorm.clonestagram.like.service;

import com.goorm.clonestagram.post.domain.Posts;
import com.goorm.clonestagram.post.repository.PostsRepository;
import com.goorm.clonestagram.like.domain.Like;
import com.goorm.clonestagram.like.repository.LikeRepository;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.goorm.clonestagram.post.ContentType.IMAGE;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class LikeServiceIntegrationTest2 {
    private static final Logger logger = LoggerFactory.getLogger(LikeServiceIntegrationTest2.class);

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private LikeRepository likeRepository;

    @BeforeEach
    public void setUp() {
        likeRepository.deleteAll();
        postsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testMultiUserLikes() {
        int[] userCounts = {5, 10, 50, 100};

        for (int userCount : userCounts) {
            logger.info("===== 테스트 시작: 사용자 {} 명 =====", userCount);

            User postOwner = createTestUser("postOwner", generateUniqueEmail());
            Posts post = createTestPost(postOwner);
            List<User> users = new ArrayList<>();

            for (int i = 0; i < userCount; i++) {
                users.add(createTestUser("user" + i, generateUniqueEmail()));
            }

            Random random = new Random();
            int likersCount = (int) (userCount * (0.7 + random.nextDouble() * 0.2));
            Set<Long> likedUserIds = new HashSet<>();

            for (int i = 0; i < userCount; i++) {
                User liker = users.get(i);

                if (likedUserIds.size() < likersCount &&
                        random.nextDouble() < 0.8 &&
                        !likedUserIds.contains(liker.getId())) {

                    logger.info("사용자 {} ({})가 좋아요를 시도 중", liker.getUsername(), liker.getId());
                    likeService.toggleLike(liker.getId(), post.getId());
                    likedUserIds.add(liker.getId());

                    // 최대 1초 동안 좋아요 반영 여부 확인
                    boolean liked = false;
                    for (int retry = 0; retry < 10; retry++) {
                        if (likeRepository.existsByUserIdAndPostsId(liker.getId(), post.getId())) {
                            liked = true;
                            break;
                        }
                        try {
                            Thread.sleep(100); // 0.1초 대기 후 재확인
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    if (liked) {
                        logger.info("사용자 {} ({}) 좋아요 완료", liker.getUsername(), liker.getId());
                    } else {
                        logger.warn("⚠ 사용자 {} ({}) 좋아요 실패", liker.getUsername(), liker.getId());
                    }
                }
            }

            long likeCount = likeService.getLikeCount(post.getId());
            logger.info("사용자 수: {}, 예상 좋아요 수: {}, 실제 좋아요 수: {}", userCount, likersCount, likeCount);

            assertTrue(Math.abs(likersCount - likeCount) <= 2,
                    "좋아요 개수가 예상된 수와 거의 일치해야 합니다. (사용자 수: " + userCount + ")");

            long uniqueLikeCount = likeRepository.findByPostsId(post.getId()).size();
            assertEquals(likeCount, uniqueLikeCount, "중복된 좋아요가 없어야 합니다.");

            likeRepository.deleteAll();
            logger.info("===== 테스트 종료: 사용자 {} 명 =====\n", userCount);
        }
    }

    // 헬퍼 메서드들 (이전과 동일)
    private String generateUniqueEmail() {
        return UUID.randomUUID().toString() + "@example.com";
    }

    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setDeleted(false);
        return userRepository.save(user);
    }

    private Posts createTestPost(User user) {
        Posts post = new Posts();
        post.setUser(user);
        post.setContent("Test Post");
        post.setContentType(IMAGE);
        post.setMediaName("test-url");
        post.setDeleted(false);
        return postsRepository.save(post);
    }
}