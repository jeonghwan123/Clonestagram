package com.goorm.clonestagram.follow.repository;

import com.goorm.clonestagram.follow.domain.Follows;
import com.goorm.clonestagram.user.domain.User;
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
    List<Follows> findByFromUser(User fromUser);

    // 특정 사용자의 팔로워 목록 조회 (나를 팔로우한 사람들)
    List<Follows> findByToUser(User toUser);


    // 특정 사용자의 팔로워 수 가져오기
    @Query("SELECT COUNT(f) FROM Follows f WHERE f.toUser.id = :userId")
    int getFollowerCount(@Param("userId") Long userId);

    // 특정 사용자의 팔로잉 수 가져오기
    @Query("SELECT COUNT(f) FROM Follows f WHERE f.fromUser.id = :userId")
    int getFollowingCount(@Param("userId") Long userId);
}
