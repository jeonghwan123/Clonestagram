package com.goorm.clonestagram.like.repository;

import com.goorm.clonestagram.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPostsId(Long postId); // 특정 게시물에 대한 좋아요 조회
    Boolean existsByUserIdAndPostsId(Long userId, Long postId); // 포스트 아이디와 유저에 대한 좋아요 조회 (수정)
    Long countByPostsId(Long postId); // 좋아요 개수 확인
    Optional<Like> findByUserIdAndPostsId(Long userId, Long postId);


}
