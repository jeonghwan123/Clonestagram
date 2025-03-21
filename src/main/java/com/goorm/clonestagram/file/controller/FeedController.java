package com.goorm.clonestagram.file.controller;

import com.goorm.clonestagram.file.dto.FeedResDto;
import com.goorm.clonestagram.file.service.FeedService;
import com.goorm.clonestagram.util.TempUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 피드 조회 요청을 처리하는 컨트롤러
 * - 클라이언트로부터 피드 조회 요청을 받아 검증 및 서비스 호출을 수행
 */
@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * 본인 피드 조회
     * - 로그인 한 유저의 Id를 조회한 후 서비스 계층에 전달
     *
     * @param userDetail 로그인 한 유저에 대한 정보
     * @param pageable 페이징 처리
     * @return 조회 성공시 서비스 호출한 유저 정보와 피드 리스트 반환
     */
    //Todo TempUserDetail 변경
    @GetMapping("/feeds/me")
    public ResponseEntity<FeedResDto> myFeed(@AuthenticationPrincipal TempUserDetail userDetail,
                                             @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable

    ){
        Long userId = userDetail.getId();

        return ResponseEntity.ok(feedService.getMyFeed(userId,pageable));
    }

    /**
     * 모든 피드 조회
     * - 모든 피드 조회 요청을 서비스 계층에 전달
     *
     * @param pageable 페이징 처리
     * @return 조회 성공시 서비스 호출한 유저 정보와 피드 리스트 반환
     */
    //Todo TempUserDetail 변경
    @GetMapping("/feeds/all")
    public ResponseEntity<FeedResDto> allFeed(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){

        return ResponseEntity.ok(feedService.getAllFeed(pageable));
    }

    /**
     * 팔로우 피드 조회
     * - 로그인 한 유저의 Id를 조회한 후 서비스 계층에 전달
     *
     * @param userDetail 로그인 한 유저에 대한 정보
     * @param pageable 페이징 처리
     * @return 조회 성공시 서비스 호출한 유저 정보와 피드 리스트 반환
     */
    //Todo TempUserDetail 변경
    @GetMapping("/feeds/follow")
    public ResponseEntity<FeedResDto> followFeed(@AuthenticationPrincipal TempUserDetail userDetail,
                                                 @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        Long userId = userDetail.getId();

        return ResponseEntity.ok(feedService.getFollowFeed(userId, pageable));
    }
}
