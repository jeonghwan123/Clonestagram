package com.goorm.clonestagram.file.service;

import com.goorm.clonestagram.file.domain.Posts;
import com.goorm.clonestagram.file.dto.FeedResDto;
import com.goorm.clonestagram.file.dto.PostInfoDto;
import com.goorm.clonestagram.file.repository.PostsRepository;
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

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    public FeedResDto getMyFeed(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("userId = " + userId + " 인 유저가 존재하지 않습니다"));

        Page<Posts> myFeed = postsRepository.findAllByUserId(user.getId(), pageable);

        return FeedResDto.builder()
                .user(UserProfileDto.fromEntity(user))
                .feed(myFeed.map(PostInfoDto::fromEntity))
                .build();
    }

    public FeedResDto getAllFeed(Pageable pageable) {
        Page<Posts> myFeed = postsRepository.findAll(pageable);

        return FeedResDto.builder()
                .user(null)
                .feed(myFeed.map(PostInfoDto::fromEntity))
                .build();
    }

    public FeedResDto getFollowFeed(Long userId, Pageable pageable) {
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

        //Todo : Follow추가후 해제
//        List<Long> followList = userRepository.findFollowingUserIdsByFromUserId(user.getId());
//        Page<Posts> postsLists = postsRepository.findAllByUserIdIn(followList, pageable);

        return FeedResDto.builder()
                .user(UserProfileDto.fromEntity(user))
//                .feed(postsLists.map(PostInfoDto::fromEntity))
                .build();
    }
}
