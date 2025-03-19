package com.goorm.clonestagram.file.repository;

import com.goorm.clonestagram.file.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 사용자 정보를 처리하는 리포지토리 인터페이스
 * - 사용자와 관련된 데이터베이스 작업을 수행
 * - JpaRepository를 상속하여 기본적인 CRUD 연산을 제공
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
