package com.goorm.clonestagram.feed.repository;

import com.goorm.clonestagram.feed.domain.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

    /**
     * 유저의 피드를 게시물 + 작성자(user)까지 함께 페치 조인하여 조회 (페이징 지원)
     */
    @Query("SELECT f FROM Feed f " +
            "JOIN FETCH f.post p " +
            "JOIN FETCH p.user u " +
            "WHERE f.user.id = :userId " +
            "ORDER BY f.createdAt DESC")
    Page<Feed> findByUserIdWithPostAndUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * 사용자가 확인한 게시물들을 피드에서 삭제
     */
    @Modifying
    @Query("DELETE FROM Feed f WHERE f.user.id = :userId AND f.post.id IN :postIds")
    void deleteByUserIdAndPostIdIn(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);

    /**
     * 특정 유저의 전체 피드 조회 (테스트 또는 삭제용)
     */
    List<Feed> findByUserId(Long userId);

    void deleteByPostId(Long postId);
}
