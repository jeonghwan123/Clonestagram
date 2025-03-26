package com.goorm.clonestagram.hashtag.repository;

import com.goorm.clonestagram.post.domain.Posts;
import com.goorm.clonestagram.hashtag.entity.PostHashTags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostHashTagRepository extends JpaRepository<PostHashTags, Long> {
    @Query("SELECT ph.posts FROM PostHashTags ph WHERE ph.hashTags.tagContent LIKE %:keyword% AND ph.posts.deleted = false order by ph.posts.createdAt desc ")
    Page<Posts> findPostsByHashtagKeyword(@Param("keyword") String keyword, Pageable pageable);

    void deleteAllByPostsId(Long id);
}
