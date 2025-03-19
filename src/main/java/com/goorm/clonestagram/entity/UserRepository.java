package com.goorm.clonestagram.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CommentEntity, Long> {
}