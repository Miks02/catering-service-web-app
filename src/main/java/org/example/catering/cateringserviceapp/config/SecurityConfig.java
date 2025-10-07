package org.example.catering.cateringserviceapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/").permitAll()
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/uploads/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("CLIENT")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/public/login")
                        .loginProcessingUrl("/public/login")
                        .defaultSuccessUrl("/public")
                        .failureUrl("/public/login?error=true"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/public/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}