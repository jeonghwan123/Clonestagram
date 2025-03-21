package com.goorm.clonestagram.follow.service;

import com.goorm.clonestagram.follow.domain.Follows;
import com.goorm.clonestagram.follow.dto.FollowDto;
import com.goorm.clonestagram.follow.repository.FollowRepository;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void toggleFollow(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우하는 사용자를 찾을 수 없습니다."));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 받을 사용자를 찾을 수 없습니다."));

        followRepository.findByFromUserAndToUser(fromUser, toUser)
                .ifPresentOrElse(follows -> {
                    // 이미 팔로우 상태라면 언팔로우
                    followRepository.delete(follows);
                }, () -> {
                    // 팔로우 상태가 아니면 팔로우 처리
                    Follows follows = new Follows(fromUser, toUser);
                    followRepository.save(follows);
                });
    }

    public List<FollowDto> getFollowingList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // followRepository에서 결과가 없다면 빈 리스트 반환
        List<Follows> followsList = followRepository.findByFromUser(user);
        if (followsList == null || followsList.isEmpty()) {
            return Collections.emptyList();  // 빈 리스트 반환
        }

        return followsList.stream()
                .map(f -> new FollowDto(
                        f.getId(),
                        f.getFromUser().getId(),
                        f.getToUser().getId(),
                        f.getFromUser().getUsername(), // 팔로우 하는 유저 이름
                        f.getToUser().getUsername(),   // 팔로우 받는 유저 이름
                        f.getFromUser().getProfileimg(), // 팔로우 하는 유저 프로필 이미지
                        f.getToUser().getProfileimg(),   // 팔로우 받는 유저 프로필 이미지
                        f.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<FollowDto> getFollowerList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // followRepository에서 결과가 없다면 빈 리스트 반환
        List<Follows> followsList = followRepository.findByToUser(user);
        if (followsList == null || followsList.isEmpty()) {
            return Collections.emptyList();  // 빈 리스트 반환
        }

        return followsList.stream()
                .map(f -> new FollowDto(
                        f.getId(),
                        f.getFromUser().getId(),
                        f.getToUser().getId(),
                        f.getFromUser().getUsername(), // 팔로우 하는 유저 이름
                        f.getToUser().getUsername(),   // 팔로우 받는 유저 이름
                        f.getFromUser().getProfileimg(), // 팔로우 하는 유저 프로필 이미지
                        f.getToUser().getProfileimg(),   // 팔로우 받는 유저 프로필 이미지
                        f.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
