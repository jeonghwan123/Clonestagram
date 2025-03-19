package com.goorm.clonestagram.repository;

import com.goorm.clonestagram.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CommentEntity, Long> {
}