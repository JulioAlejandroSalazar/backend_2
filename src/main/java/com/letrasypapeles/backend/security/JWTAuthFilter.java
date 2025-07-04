package com.letrasypapeles.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpHeaders;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String tokenJWT = getTokenFromRequestJWTAuth(request);
        final String username_request;

        if (tokenJWT == null)
        {
            filterChain.doFilter(request, response);
            return;
        }
        username_request = jwtService.getUsernameFromToken(tokenJWT);
        if (username_request!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails=userDetailsService.loadUserByUsername(username_request);
            if (jwtService.isTokenValid(tokenJWT, userDetails))
            {
                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
                        userDetails,null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequestJWTAuth(HttpServletRequest request) {
        final String authHeaderJWT=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeaderJWT) && authHeaderJWT.startsWith("Bearer "))
        {
            return authHeaderJWT.substring(7);
        }
        return null;
    }
}
