package dev.kavrin.spring_boot_crash_course.controller

import dev.kavrin.spring_boot_crash_course.security.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication & token management")
class AuthController(
    private val authService: AuthService,
) {

    @Schema(description = "Credentials payload for registration & login")
    data class AuthRequest(
        @Schema(example = "user@example.com")
        @field:Email(message = "Invalid email format.")
        val email: String,
        @Schema(example = "P@ssw0rd123")
        @field:Pattern(
            regexp = "^[a-zA-Z0-9_]{1,63}$",
            message = "Password must be at least 9 characters."
        )
        val password: String,
    )

    @Schema(description = "Refresh token payload")
    data class RefreshRequest(
        @Schema(description = "Previously issued refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        val refreshToken: String,
    )

    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a user account with the provided email & password.",
        security = [], // Public endpoint – override global security requirement
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User registered successfully."
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation or duplicate email error",
                content = [Content()]
            )
        ]
    )
    fun register(
        @Valid @RequestBody body: AuthRequest
    ) {
        authService.register(body.email, body.password)
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login and obtain tokens",
        description = "Authenticates the user and returns an access & refresh token pair.",
        security = [],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successful authentication",
                content = [
                    Content(schema = Schema(implementation = AuthService.TokenPair::class))
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = [Content()]
            )
        ]
    )
    fun login(
        @Valid @RequestBody body: AuthRequest
    ): AuthService.TokenPair {
        return authService.login(body.email, body.password)
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh access token",
        description = "Exchanges a valid refresh token for a new access & refresh token pair (rotation).",
        security = [],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Tokens refreshed",
                content = [
                    Content(schema = Schema(implementation = AuthService.TokenPair::class))
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid or expired refresh token",
                content = [Content()]
            )
        ]
    )
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthService.TokenPair {
        return authService.refresh(body.refreshToken)
    }
}