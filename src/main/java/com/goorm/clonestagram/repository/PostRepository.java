package com.goorm.clonestagram.repository;

import com.goorm.clonestagram.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<CommentEntity, Long> {

}