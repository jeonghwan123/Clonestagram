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
    Page<Posts> findAllByUserId(Long userId, Pageable pageable);

}
