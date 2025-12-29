package com.example.mobile.config;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.mobile.auth.utils.JwtUtil;
import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extract user information
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String oauthId = oauth2User.getName();
        
        // Determine provider from registration ID
        String provider = determineProvider(request);
        
        // Find or create user
        UserEntity user = findOrCreateUser(email, name, provider, oauthId);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // Set HTTP-only cookie with the token
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .domain(".khunmeas.site")
            .maxAge(60 * 60 * 24 * 7)
            .sameSite("None")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
        
        // Redirect to frontend without params
        getRedirectStrategy().sendRedirect(request, response, frontendUrl + "/");
        
        // Uncomment below for JSON response (comment out redirect above)
        // response.setContentType("application/json");
        // response.setCharacterEncoding("UTF-8");
        // response.getWriter().write(String.format(
        //     "{\"success\":true,\"message\":\"OAuth2 login successful\",\"token\":\"%s\",\"username\":\"%s\",\"email\":\"%s\",\"provider\":\"%s\"}",
        //     token, user.getUsername(), user.getEmail(), provider
        // ));
    }

    private UserEntity findOrCreateUser(String email, String name, String provider, String oauthId) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();
            user.setOauthProvider(provider);
            user.setOauthId(oauthId);
            user.setLastLogin(Instant.now());
            return userRepository.save(user);
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setUsername(name != null ? name.replaceAll("\\s+", "") : email.split("@")[0]);
            newUser.setOauthProvider(provider);
            newUser.setOauthId(oauthId);
            newUser.setRoles("USER");
            newUser.setEnabled(true);
            newUser.setOtpVerified(true); // OAuth users are pre-verified
            newUser.setPassword(passwordEncoder.encode(generateRandomPassword()));
            newUser.setLastLogin(Instant.now());
            return userRepository.save(newUser);
        }
    }

    private String determineProvider(HttpServletRequest request) {
        // Check the request URI for the registration ID
        String requestUri = request.getRequestURI();
        if (requestUri != null) {
            if (requestUri.contains("/google")) return "google";
            if (requestUri.contains("/github")) return "github";
            if (requestUri.contains("/telegram")) return "telegram";
        }
        
        // Fallback: check referer header
        String referer = request.getHeader("referer");
        if (referer != null) {
            if (referer.contains("google")) return "google";
            if (referer.contains("github")) return "github";
            if (referer.contains("telegram")) return "telegram";
        }
        return "unknown";
    }

    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString();
    }
}
