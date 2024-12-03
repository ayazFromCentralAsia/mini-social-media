package com.example.social.media.Config.JWT;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserRepository userRepository;



    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            if (StringUtils.hasText(token)) {
                try {
                    String email;
                    try {
                        email = jwtService.validateToken(token);
                    } catch (MalformedJwtException e) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неверный JWT токен: " + e.getMessage());
                        return;
                    } catch (ExpiredJwtException e) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Токен истек: " + e.getMessage());
                        return;
                    }
                    User account = userRepository.findByEmail(email).orElseThrow(() ->
                            new RuntimeException("Пользователь с такой почтой %s не найден!".formatted(email)));
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    account.getUsername(),
                                    null,
                                    account.getAuthorities()
                            ));
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неправильный токен! " + e.getMessage());
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
