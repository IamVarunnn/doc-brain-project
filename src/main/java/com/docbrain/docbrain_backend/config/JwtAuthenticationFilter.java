package com.docbrain.docbrain_backend.config;

import com.docbrain.docbrain_backend.respositry.UserRepository;
import com.docbrain.docbrain_backend.service.CustomUserDetail;
import com.docbrain.docbrain_backend.service.CustomUserDetailService;
import com.docbrain.docbrain_backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tika.sax.SecureContentHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserDetailService userDetailService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // logic to implement validation

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String username;

        try {
            username = jwtService.extractUserName(token);

            if(username == null || !jwtService.isTokenValid(token, userDetailService.loadUserByUsername(username))){
                filterChain.doFilter(request, response);
                return;
            }

            var user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, new CustomUserDetail(user).getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception e){
            filterChain.doFilter(request, response);
            e.printStackTrace();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
