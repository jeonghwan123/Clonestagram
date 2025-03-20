package com.goorm.clonestagram.comment.controller;

import com.goorm.clonestagram.comment.dto.CommentRequest;
import com.goorm.clonestagram.comment.dto.CommentResponse;
import com.goorm.clonestagram.comment.domain.CommentEntity;
import com.goorm.clonestagram.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/")
    public String index() {
        log.info("index");
        return "Welcome to Clonestagram!";
    }

    @GetMapping("/post/{id}")
    public CommentResponse getCommentById(@PathVariable Long id){
        CommentEntity entity = commentService.getCommentById(id);

        if(entity == null){ return CommentResponse.builder().build(); }
        return CommentResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .postId(entity.getPostId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();

    }

    @GetMapping("/post/{postId}")
    public List<CommentResponse> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentEntity> entities = commentService.getCommentsByPostId(postId);

        // ✅ List<CommentEntity> → List<CommentResponse> 변환
        return entities.stream()
                .map(entity -> CommentResponse.builder()
                        .id(entity.getId())
                        .userId(entity.getUserId())
                        .postId(entity.getPostId())
                        .content(entity.getContent())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }



    @PostMapping
    public CommentResponse create(@RequestBody CommentRequest request) throws Exception{
        CommentEntity entity = commentService.createCommentWithRollback(
                CommentEntity.builder()
                        .userId(request.getUserId())
                        .postId(request.getPostId())
                        .content(request.getContent())
                        .createdAt(request.getCreatedAt())
                        .build()
        );

        return CommentResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .postId(entity.getPostId())
                .content(entity.getContent())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();

    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long requesterId) {
        commentService.removeComment(commentId, requesterId);
        return "댓글이 삭제되었습니다. ID: " + commentId;
    }
}
