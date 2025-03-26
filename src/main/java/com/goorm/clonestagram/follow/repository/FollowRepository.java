package com.goorm.clonestagram.follow.repository;

import com.goorm.clonestagram.follow.domain.Follows;
import com.goorm.clonestagram.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follows, Long> {


    // 팔로우 관계 조회 메서드 추가
    Optional<Follows> findByFromUserAndToUser(User fromUser, User toUser);

    // 특정 사용자의 팔로잉 목록 조회 (내가 팔로우한 사람들)
    @Query("SELECT f FROM Follows f WHERE f.fromUser = :fromUser AND f.fromUser.deleted = false AND f.toUser.deleted = false")
    List<Follows> findByFromUserAndDeletedIsFalse(@Param("fromUser") User fromUser);

    // 특정 사용자의 팔로워 목록 조회 (나를 팔로우한 사람들)
    @Query("SELECT f FROM Follows f WHERE f.toUser = :toUser AND f.fromUser.deleted = false AND f.toUser.deleted = false")
    List<Follows> findByToUserAndDeletedIsFalse(@Param("toUser") User toUser);

    @Query("SELECT f.fromUser.id FROM Follows f WHERE f.toUser.id = :userId")
    List<Long> findFollowerIdsByToUserId(@Param("userId") Long userId);

    // 특정 사용자의 팔로워 수 가져오기
    @Query("SELECT COUNT(f) FROM Follows f WHERE f.toUser.id = :userId AND f.toUser.deleted = false")
    int getFollowerCount(@Param("userId") Long userId);

    // 특정 사용자의 팔로잉 수 가져오기
    @Query("SELECT COUNT(f) FROM Follows f WHERE f.fromUser.id = :userId AND f.fromUser.deleted = false")
    int getFollowingCount(@Param("userId") Long userId);

    @Query("SELECT f.toUser FROM Follows f WHERE f.fromUser.id = :fromUserId AND f.toUser.username LIKE %:keyword% AND f.toUser.deleted = false")
    Page<User> findFollowingByKeyword(@Param("fromUserId") Long fromUserId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT f.fromUser FROM Follows f WHERE f.toUser.id = :toUserId AND f.fromUser.username LIKE %:keyword% AND f.fromUser.deleted = false")
    Page<User> findFollowerByKeyword(@Param("toUserId") Long toUserId, @Param("keyword") String keyword, Pageable pageable);


}
