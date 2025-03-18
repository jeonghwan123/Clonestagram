package com.goorm.clonestagram.file.repository;

import com.goorm.clonestagram.file.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Posts와 관련된 JPA
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
