package dev.kavrin.spring_boot_crash_course.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(httpSec: HttpSecurity): SecurityFilterChain {
        return httpSec
            .csrf { csrfConf -> csrfConf.disable() }
            .sessionManagement { sessionConf -> sessionConf.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
    }
}