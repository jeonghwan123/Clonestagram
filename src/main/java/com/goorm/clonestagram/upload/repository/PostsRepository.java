package com.goorm.clonestagram.upload.repository;

import com.goorm.clonestagram.upload.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Posts와 관련된 JPA
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
