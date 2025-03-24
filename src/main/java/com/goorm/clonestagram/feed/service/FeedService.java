package com.goorm.clonestagram.feed.service;

import com.goorm.clonestagram.feed.domain.Feed;
import com.goorm.clonestagram.feed.dto.FeedResponseDto;
import com.goorm.clonestagram.feed.repository.FeedRepository;
import com.goorm.clonestagram.post.domain.Posts;
import com.goorm.clonestagram.follow.domain.Follows;
import com.goorm.clonestagram.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FollowRepository followRepository;
    /**
     * 특정 사용자의 피드를 페이징하여 조회합니다.
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호 (0부터 시작)
     * @param size   페이지당 항목 수
     * @return 페이징된 FeedResponseDto 리스트
     */
    @Transactional
    public Page<FeedResponseDto> getUserFeed(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Feed> feeds = feedRepository.findByUserIdWithPostAndUser(userId, pageable);

        return feeds.map(FeedResponseDto::from);
    }

    /**
     * 사용자가 피드에서 게시물을 확인한 경우 해당 Feed 데이터를 삭제합니다.
     *
     * @param userId   현재 사용자 ID
     * @param postIds  확인한 게시물 ID 목록
     */
    @Transactional
    public void removeSeenFeeds(Long userId, List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) return;
        feedRepository.deleteByUserIdAndPostIdIn(userId, postIds);
    }

    /**
     * (선택) 사용자 피드 전체 삭제 - 테스트용 또는 계정 삭제 시 사용
     */
    @Transactional
    public void deleteAllByUser(Long userId) {
        List<Feed> userFeeds = feedRepository.findByUserId(userId);
        feedRepository.deleteAll(userFeeds);
    }

    // 게시물이 업로드될 때: 팔로워들의 피드 생성
    public void createFeedForFollowers(Posts post) {
        List<Long> followerIds = followRepository.findFollowerIdsByToUserId(post.getUser().getId());
        if (followerIds == null || followerIds.isEmpty()) {
            return; // ✅ 팔로워 없으면 저장하지 않음
        }

        List<Feed> feeds = followerIds.stream()
                .map(followerId -> new Feed(followerId, post.getId()))
                .toList();

        feedRepository.saveAll(feeds);
    }

    // 게시물이 삭제될 때: 해당 게시물에 대한 피드 전부 삭제
    public void deleteFeedByPostId(Long postId) {
        feedRepository.deleteByPostId(postId);
    }
}
