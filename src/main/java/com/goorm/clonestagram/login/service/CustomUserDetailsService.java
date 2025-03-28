package com.goorm.clonestagram.login.service;


import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.repository.UserRepository;
import com.goorm.clonestagram.util.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("이메일을 찾을 수 없습니다.");
        }

        return new CustomUserDetails(user); // ✅ 반드시 이걸로
    }
}

