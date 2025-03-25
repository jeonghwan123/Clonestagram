package com.goorm.clonestagram.feed.domain;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "follows")
public class Follow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users follower;

    @ManyToOne
    private Users following;

    private LocalDateTime createdAt;

    // 생략
}

