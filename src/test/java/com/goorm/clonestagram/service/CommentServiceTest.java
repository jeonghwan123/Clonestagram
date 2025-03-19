package com.goorm.clonestagram.service;

import com.goorm.clonestagram.entity.CommentEntity;
import com.goorm.clonestagram.entity.CommentRepository;
import com.goorm.clonestagram.entity.PostRepository;
import com.goorm.clonestagram.entity.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.goorm.clonestagram.util.CustomTestLogger;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CustomTestLogger.class})
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    private CommentEntity mockComment;

    @BeforeEach
    void setUp() {
        mockComment = CommentEntity.builder()
                .id(1L)
                .userId(100L)
                .postId(200L)
                .content("Test Comment")
                .build();
    }

    /**
     * ✅ 댓글을 성공적으로 저장하는 테스트
     */
    @Test
    void createComment_ShouldSaveComment() {
        // Given: commentRepository.save()가 실행될 때 mockComment를 반환하도록 설정
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(mockComment);

        // When: 새로운 댓글을 생성
        CommentEntity savedComment = commentService.createCommentWithRollback(mockComment);

        // Then: 저장된 댓글이 예상대로 반환되는지 확인
        assertNotNull(savedComment);
        assertEquals(mockComment.getId(), savedComment.getId());
        assertEquals(mockComment.getContent(), savedComment.getContent());

        // Verify: commentRepository.save()가 1번 호출되었는지 확인
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    /**
     * ✅ 존재하지 않는 userId로 댓글 작성 테스트
     */
    @Test
    void createComment_ShouldThrowException_WhenUserDoesNotExist() {
        // Given: 모든 userId에 대해 false 반환
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // When & Then: 예외 발생 여부 확인
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.createComment(mockComment));

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("존재하지 않는 사용자 ID입니다"));

        // Verify: userRepository.existsById()가 1번 호출되었는지 확인
        verify(userRepository, times(1)).existsById(anyLong());
        // Verify: commentRepository.save()가 호출되지 않았는지 확인
        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    /**
     * ✅ 존재하지 않는 postId로 댓글 작성 테스트
     */
    @Test
    void createComment_ShouldThrowException_WhenPostDoesNotExist() {
        // Given: 특정 postId (mockComment의 postId)에 대해 false 반환 (게시글이 존재하지 않도록 설정)
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(postRepository.existsById(mockComment.getPostId())).thenReturn(false);

        // When & Then: 예외 발생 여부 확인
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.createComment(mockComment));

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("존재하지 않는 게시글 ID입니다"));

        // Verify: postRepository.existsById()가 1번 호출되었는지 확인
        verify(postRepository, times(1)).existsById(mockComment.getPostId());
        // Verify: commentRepository.save()가 호출되지 않았는지 확인
        verify(commentRepository, never()).save(any(CommentEntity.class));
    }


    /**
     * ✅ 존재하는 댓글을 조회하는 테스트
     */
    @Test
    void getCommentById_ShouldReturnComment_WhenCommentExists() {
        // Given: 특정 ID로 조회할 때 mockComment 반환
        when(commentRepository.findById(1L)).thenReturn(Optional.of(mockComment));

        // When: 댓글을 ID로 조회
        CommentEntity foundComment = commentService.getCommentById(1L);

        // Then: 댓글이 정상적으로 조회되는지 확인
        assertNotNull(foundComment);
        assertEquals(1L, foundComment.getId());
        assertEquals("Test Comment", foundComment.getContent());

        // Verify: commentRepository.findById()가 1번 호출되었는지 확인
        verify(commentRepository, times(1)).findById(1L);
    }

    /**
     * ✅ 존재하지 않는 댓글을 조회할 때 예외 발생하는지 테스트
     */
    @Test
    void getCommentById_ShouldThrowException_WhenCommentNotFound() {
        // Given: 해당 ID의 댓글이 존재하지 않도록 설정
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then: 예외 메시지도 함께 검증
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.getCommentById(999L));

        // 예외 메시지 검증
        String expectedMessage = "해당 댓글이 존재하지 않습니다: 999";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // Verify: findById()가 1번 호출되었는지 확인
        verify(commentRepository, times(1)).findById(999L);
    }
}
