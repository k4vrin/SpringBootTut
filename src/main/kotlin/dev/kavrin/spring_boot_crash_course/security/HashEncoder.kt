package dev.kavrin.spring_boot_crash_course.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {

    private val bcryptEncoder = BCryptPasswordEncoder()

    fun encode(raw: String): String = bcryptEncoder.encode(raw)

    fun matches(raw: String, hashed: String): Boolean = bcryptEncoder.matches(raw, hashed)
}