package com.goorm.clonestagram.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<CommentEntity, Long> {
}