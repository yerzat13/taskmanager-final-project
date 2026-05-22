package com.taskmanager.security;

import com.taskmanager.entity.AbdrassulayevYerzatUser;
import com.taskmanager.repository.AbdrassulayevYerzatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbdrassulayevYerzatUserDetailsServiceImpl implements UserDetailsService {

    private final AbdrassulayevYerzatUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AbdrassulayevYerzatUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        log.debug("User found: {}", username);
        return new AbdrassulayevYerzatCustomUserDetails(user);
    }
}