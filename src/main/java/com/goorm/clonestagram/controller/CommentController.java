package com.goorm.clonestagram.controller;

import com.goorm.clonestagram.controller.model.CommentRequest;
import com.goorm.clonestagram.controller.model.CommentResponse;
import com.goorm.clonestagram.entity.CommentEntity;
import com.goorm.clonestagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("post/{id}")
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
}
