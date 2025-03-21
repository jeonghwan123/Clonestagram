package com.goorm.clonestagram.like.service;

import com.goorm.clonestagram.file.domain.Posts;
import com.goorm.clonestagram.file.repository.PostsRepository;
import com.goorm.clonestagram.like.domain.Like;
import com.goorm.clonestagram.like.repository.LikeRepository;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostsRepository postRepository;

    @InjectMocks
    private LikeService likeService;

    private User user;
    private Posts post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Test user
        user = new User();
        user.setId(1L);
        user.setUsername("user1");

        // Test post
        post = new Posts();
        post.setId(1L);
        post.setContent("Test Post");
    }

    @Test
    public void testToggleLikeAddLike() {
        // Given: User and post are available, and no existing like in the database.
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(likeRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.empty());  // No like exists

        // When: User toggles like
        likeService.toggleLike(1L, 1L);

        // Then: The like should be saved
        verify(likeRepository, times(1)).save(any(Like.class));
        verify(likeRepository, times(0)).delete(any(Like.class));
    }

    @Test
    public void testToggleLikeRemoveLike() {
        // Given: User and post are available, and existing like is found in the database.
        Like existingLike = new Like();
        existingLike.setUser(user);
        existingLike.setPost(post);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(likeRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(Optional.of(existingLike));  // Like already exists

        // When: User toggles like
        likeService.toggleLike(1L, 1L);

        // Then: The like should be deleted
        verify(likeRepository, times(0)).save(any(Like.class));
        verify(likeRepository, times(1)).delete(existingLike);
    }
}
