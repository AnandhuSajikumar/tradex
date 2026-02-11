package com.spring.tradex.configs;

import com.spring.tradex.DTO.UserPrincipal;
import com.spring.tradex.Models.User;
import com.spring.tradex.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig{
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            return new UserPrincipal(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                    )
            );

        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());

        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




}
