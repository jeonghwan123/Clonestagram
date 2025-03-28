package com.goorm.clonestagram.login.controller;


import com.goorm.clonestagram.login.dto.LoginForm;
import com.goorm.clonestagram.login.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final AuthenticationManager authenticationManager;

    // 로그인 처리 (REST API 방식)
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();

        // 로그인 로직 처리
        if (loginService.login(email, password)) {
            // 로그인 성공 시 성공 메시지 반환 (상태 코드 200 OK)
            // ✅ 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            // ✅ AuthenticationManager로 실제 인증 처리
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // ✅ SecurityContext에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ 세션에 SecurityContext 저장 (이거 해야 @AuthenticationPrincipal이 작동함)
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
        } else {
            // 로그인 실패 시 오류 메시지 반환 (상태 코드 401 Unauthorized)
            return new ResponseEntity<>("이메일 또는 비밀번호가 잘못되었습니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}
