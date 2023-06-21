package com.MoneyPlant.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.MoneyPlant.service.jwt.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

// OncePerRequestFilter : 요청 당 한번만 적용되도록 하는 클래스
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // jwt의 value값 가져오기
            String jwt = parseJwt(request);
            // 토큰이 정상적으로 들어오는지 체크
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 토큰으로 부터 이메일값 가져오기
                String email = jwtUtils.getEmailFromJwtToken(jwt);
                // 이메일 값으로 유저 정보 가져오기
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // UsernamepAsswordAuthenticationToken( principal, credentias, authorities )
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                // setDetails : Authentication 개체의 인증 요청에 대한 추가 세부 정보를 제공하는 데 사용됩니다
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    // request를 받아 jwt의 value값 가져오기
    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
    }
}
