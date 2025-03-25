package com.goorm.clonestagram.like.domain;

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
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts posts;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Like(User user, Posts posts) {
        this.user = user;
        this.posts = posts;
        this.createdAt = LocalDateTime.now();
    }
}