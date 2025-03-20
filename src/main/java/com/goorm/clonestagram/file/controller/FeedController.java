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


@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    //Todo TempUserDetail 변경
    @GetMapping("/feeds/me")
    public ResponseEntity<FeedResDto> myFeed(@AuthenticationPrincipal TempUserDetail userDetail,
                                             @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable

    ){
        Long userId = userDetail.getId();

        return ResponseEntity.ok(feedService.getMyFeed(userId,pageable));
    }

    //Todo TempUserDetail 변경
    @GetMapping("/feeds/all")
    public ResponseEntity<FeedResDto> allFeed(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){

        return ResponseEntity.ok(feedService.getAllFeed(pageable));
    }

}
