package com.goorm.clonestagram.upload.domain;

import com.goorm.clonestagram.upload.ContentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * Posts 엔티티
 * - 이미지, 컨텐츠 정보를 담는 테이블 매핑 클래스
 * - media 파일의 경로, 작성 내용, 작성 시간 등을 포함
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Posts {

    /**
     * 게시물 Primary Key
     * - 자동 증가 (IDENTITY 전략)
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

       /** User 객체 대기 **/
//    @ManyToOne
//    @Column(name = "user_id", nullable = false)
//    private User user;

    /**
     * 게시물 내용
     * - 이미지에 대한 설명 또는 글 내용
     */
    @Column(name = "content")
    private String content;

    /**
     * 미디어 파일명
     * - unique한 파일명이기에 select시 사용 가능
     */
    @Column(name = "media_url", nullable = false)
    private String mediaName;

    /**
     * 콘텐츠 타입
     * - 이미지, 동영상 타입 구분
     * - Enum으로 관리
     */
    @Column(name = "contents_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    /**
     * 게시물 생성 시간
     * - imageUploadReqDto.toEntity()에서 셋팅
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 게시물 수정 시간
     * - 게시물이 업데이트 될 때 변경됨
     * - 처음 생성 시 null 가능
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
