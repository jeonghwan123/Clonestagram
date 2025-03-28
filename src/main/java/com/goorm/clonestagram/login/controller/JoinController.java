package com.goorm.clonestagram.login.controller;

import com.goorm.clonestagram.login.dto.JoinDto;
import com.goorm.clonestagram.login.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    // 회원 가입 처리 (REST API 방식)
    @PostMapping("/join")
    public ResponseEntity<Object> join(@Valid @RequestBody JoinDto joinDto, BindingResult result) {
        // 유효성 검사 실패 시
        if (result.hasErrors()) {
            // 오류 메시지와 상태 코드를 반환
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        try {
            // 회원가입 처리 (JoinService에서 처리)
            joinService.joinProcess(joinDto);
            // 성공적으로 가입한 경우 로그인 페이지로 리다이렉트할 수 없으므로, 성공 메시지 반환
            return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED); // 회원가입 성공 시 201 CREATED 상태 코드 반환
        } catch (IllegalStateException e) {
            // 이미 존재하는 이메일에 대한 예외 처리
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 오류 메시지와 400 BAD_REQUEST 반환
        }
    }
}
