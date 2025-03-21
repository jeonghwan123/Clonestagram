package com.goorm.clonestagram.file.service;

import com.goorm.clonestagram.file.domain.Posts;
import com.goorm.clonestagram.file.dto.FeedResDto;
import com.goorm.clonestagram.file.dto.PostInfoDto;
import com.goorm.clonestagram.file.repository.PostsRepository;
import com.goorm.clonestagram.follow.repository.FollowRepository;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.dto.UserProfileDto;
import com.goorm.clonestagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 피드 조회 요청을 처리하는 서비스
 * - 유저의 정보를 받아 피드 조회 서비스 수행
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    /**
     * 본인 피드 조회
     * 유저 id를 활용해 유저 정보 조회
     * 해당 유저가 작성한 모든 피드 반환
     *
     * @param userId 조회를 요청한 유저
     * @param pageable 페이징 처리
     * @return 유저 정보, 해당 유저가 작성한 피드 리스트
     */
    public FeedResDto getMyFeed(Long userId, Pageable pageable) {
        //1. userId를 활용해 유저 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("userId = " + userId + " 인 유저가 존재하지 않습니다"));

        //2. 해당 유저가 작성한 모든 피드 조회, 페이징 처리
        Page<Posts> myFeed = postsRepository.findAllByUserId(user.getId(), pageable);

        //3. 모든 작업이 완료도니 경우 응답 반환
        return FeedResDto.builder()
                .user(UserProfileDto.fromEntity(user))
                .feed(myFeed.map(PostInfoDto::fromEntity))
                .build();
    }

    /**
     * 모든 피드 조회
     * 모든 유저가 작성한 피드 반환
     *
     * @param pageable 페이징 처리
     * @return 모든 유저가 작성한 피드 리스트
     */
    public FeedResDto getAllFeed(Pageable pageable) {
        //1. DB에 저장된 모든 피드 조회
        Page<Posts> myFeed = postsRepository.findAll(pageable);

        //2. 모든 작업이 완료된 경우 반환
        return FeedResDto.builder()
                .feed(myFeed.map(PostInfoDto::fromEntity))
                .build();
    }

    /**
     * 본인 피드 조회
     * 유저 id를 활용해 유저 정보 조회
     * 해당 유저가 작성한 모든 피드 반환
     *
     * @param userId 조회를 요청한 유저
     * @param pageable 페이징 처리
     * @return 유저 정보, 해당 유저가 작성한 피드 리스트
     */
    public FeedResDto getFollowFeed(Long userId, Pageable pageable) {
        //1. userId를 활용해 유저 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("userId = " + userId + " 인 유저가 존재하지 않습니다"));


//        Map<Long, Page<Posts>> followFeedMap = new HashMap<>();
//
//        List<Follow> followList = user.getFollowing();
//        for (Follow follow : followList) {
//            Long followingId = follow.getToUser().getId();
//            Page<Posts> posts = postsRepository.findAllByUserId(followingId, pageable);
//            followFeedMap.put(followingId, posts);  // 유저별로 페이지 저장
//        }
        //2. 해당 유저의 팔로우 리스트를 조회
        List<Long> followList = userRepository.findFollowingUserIdsByFromUserId(user.getId());
        //3. 팔로우 리스트에 있는 유저들의 피드들을 조회
        Page<Posts> postsLists = postsRepository.findAllByUserIdIn(followList, pageable);

        //4. 모든 작업이 완료된 경우 반환
        return FeedResDto.builder()
                .user(UserProfileDto.fromEntity(user))
                .feed(postsLists.map(PostInfoDto::fromEntity))
                .build();
    }
}
