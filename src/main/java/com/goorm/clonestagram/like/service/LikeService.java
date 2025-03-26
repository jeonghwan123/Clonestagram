package com.goorm.clonestagram.like.service;

import com.goorm.clonestagram.post.domain.Posts;
import com.goorm.clonestagram.post.repository.PostsRepository;
import com.goorm.clonestagram.like.domain.Like;
import com.goorm.clonestagram.like.repository.LikeRepository;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    // 좋아요 토글
    @Transactional
    public void toggleLike(Long userId, Long postId) {
        User user = userRepository.findByIdAndDeletedIsFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Posts post = postsRepository.findByIdAndDeletedIsFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        // userId와 postId를 사용해 좋아요 여부 확인
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostsId(userId, postId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get()); // 좋아요 취소
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPosts(post);
            likeRepository.save(like); // 좋아요 추가
        }
    }


    // 특정 게시물에 대한 좋아요 개수 조회
    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostsId(postId);
    }
}
