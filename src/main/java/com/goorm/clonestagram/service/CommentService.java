package com.goorm.clonestagram.service;

import com.goorm.clonestagram.domain.CommentEntity;
import com.goorm.clonestagram.repository.CommentRepository;
import com.goorm.clonestagram.repository.PostRepository;
import com.goorm.clonestagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentEntity createComment(@RequestBody CommentEntity comment) {

        // ✅ userId가 존재하는지 확인
        if (!userRepository.existsById(comment.getUserId())) {
            throw new IllegalArgumentException("존재하지 않는 사용자 ID입니다: " + comment.getUserId());
        }

        // ✅ postId가 존재하는지 확인
        if (!postRepository.existsById(comment.getPostId())) {
            throw new IllegalArgumentException("존재하지 않는 게시글 ID입니다: " + comment.getPostId());
        }

        return commentRepository.save(comment);
    }


    @Transactional
    public CommentEntity createCommentWithRollback(CommentEntity entity) {
        return commentRepository.save(entity);
    }


    @Transactional(readOnly = true)
    public CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + id));
    }


    @Transactional(readOnly = true)
    public List<CommentEntity> getCommentsByPostId(Long postId) {
        // postId가 실제 존재하는지 확인
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("존재하지 않는 게시글 ID입니다: " + postId);
        }

        List<CommentEntity> comments = commentRepository.findByPostId(postId);

        // 댓글이 없는 경우 예외 발생
        if (comments.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글(" + postId + ")에는 댓글이 없습니다.");
        }

        return comments;
    }


}
