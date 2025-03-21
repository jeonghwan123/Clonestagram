package com.goorm.clonestagram.file.repository;

import com.goorm.clonestagram.file.domain.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Posts와 관련된 JPA
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {
    /**
     * 본인 피드 조회
     * 특정 유저가 게시한 모든 피드를 조회
     *
     * @param userId 게시글 작성자
     * @param pageable 페이징 처리
     * @return 특정 유저가 작성한 모든 피드
     */
    Page<Posts> findAllByUserId(Long userId, Pageable pageable);

    /**
     * 팔로우들의 피드 조회
     * 특정 유저가 팔로우한 유저들의 피드를 조회
     *
     * @param followIds 팔로우한 유저들
     * @param pageable 페이징 처리
     * @return 팔로우한 유저들의 작성한 모든 피드
     */
    Page<Posts> findAllByUserIdIn(List<Long> followIds, Pageable pageable);
}
