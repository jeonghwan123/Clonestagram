package com.goorm.clonestagram.like.domain;

import com.goorm.clonestagram.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.post.domain.Posts;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Like(User user, Posts post) {
        this.user = user;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }
}