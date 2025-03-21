package com.goorm.clonestagram.follow.domain;


import com.goorm.clonestagram.common.base.BaseTimeEntity;
import com.goorm.clonestagram.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follows extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "follower")
    private User fromUser;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private User toUser;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Follows(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.createdAt = LocalDateTime.now(); // 현재 시간 자동 저장
    }



}
